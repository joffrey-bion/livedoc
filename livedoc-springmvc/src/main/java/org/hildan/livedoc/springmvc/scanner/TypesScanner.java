package org.hildan.livedoc.springmvc.scanner;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.scanner.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanner.types.TypesExplorer;
import org.hildan.livedoc.springmvc.scanner.builder.SpringRequestBodyBuilder;
import org.hildan.livedoc.springmvc.scanner.utils.ClasspathUtils;
import org.reflections.Reflections;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Sets;

class TypesScanner {

    private final Reflections reflections;

    TypesScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    Set<Class<?>> findJsondocObjects(List<String> packages) {
        Set<Type> rootTypes = getRootApiTypes();

        TypesExplorer explorer = new TypesExplorer(new FieldPropertyScanner(), reflections);
        Set<Class<?>> classes = explorer.findTypes(rootTypes);

        return classes.stream().filter(clazz -> inWhiteListedPackages(packages, clazz)).collect(Collectors.toSet());
    }

    private Set<Type> getRootApiTypes() {
        Set<Type> types = Sets.newHashSet();
        Set<Method> methodsToDocument = getMethodsToDocument();
        for (Method method : methodsToDocument) {
            types.add(method.getGenericReturnType());
            Type requestBodyType = getBodyParamType(method);
            if (requestBodyType != null) {
                types.add(requestBodyType);
            }
        }
        return types;
    }

    private Set<Method> getMethodsToDocument() {
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(RequestMapping.class);
        if (ClasspathUtils.isSubscribeMappingOnClasspath()) {
            methodsAnnotatedWith.addAll(reflections.getMethodsAnnotatedWith(SubscribeMapping.class));
        }
        if (ClasspathUtils.isMessageMappingOnClasspath()) {
            methodsAnnotatedWith.addAll(reflections.getMethodsAnnotatedWith(MessageMapping.class));
        }
        return methodsAnnotatedWith;
    }

    private Type getBodyParamType(Method method) {
        int bodyParamIndex = SpringRequestBodyBuilder.getIndexOfBodyParam(method);
        if (bodyParamIndex < 0) {
            return null;
        }
        return method.getGenericParameterTypes()[bodyParamIndex];
    }

    private boolean inWhiteListedPackages(List<String> packages, Class<?> clazz) {
        Package p = clazz.getPackage();
        return p != null && packages.stream().anyMatch(whiteListedPkg -> p.getName().startsWith(whiteListedPkg));
    }
}
