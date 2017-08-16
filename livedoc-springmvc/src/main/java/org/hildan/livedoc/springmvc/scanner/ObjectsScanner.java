package org.hildan.livedoc.springmvc.scanner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.springmvc.scanner.builder.SpringRequestBodyBuilder;
import org.hildan.livedoc.springmvc.scanner.utils.ClasspathUtils;
import org.hildan.livedoc.springmvc.scanner.utils.GenericTypeExplorer;
import org.reflections.Reflections;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Sets;

public class ObjectsScanner {

    /**
     * Subtypes of these types should not appear in the doc, even if they fall under the white-listed packages. This
     * applies in particular to container types like collections and maps, which don't need to have a documented
     * implementation.
     * <p>
     * Note: this is to preserve the original behaviour of JsonDoc, but might very well be reconsidered later on.
     */
    private static final Class<?>[] IGNORED_PARENT_TYPES = {Map.class, Collection.class};

    private final Reflections reflections;

    public ObjectsScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Set<Class<?>> findJsondocObjects(List<String> packages) {
        Set<Class<?>> candidates = getRootApiObjects();
        Set<Class<?>> subCandidates = Sets.newHashSet();

        // This is to get objects' fields that are not returned nor part of the body request of a method, but that
        // are a field of an object returned or a body  of a request of a method
        for (Class<?> clazz : candidates) {
            appendSubCandidates(clazz, subCandidates);
        }
        candidates.addAll(subCandidates);

        return candidates.stream().filter(clazz -> inWhiteListedPackages(packages, clazz)).collect(Collectors.toSet());
    }

    private Set<Class<?>> getRootApiObjects() {
        Set<Class<?>> candidates = Sets.newHashSet();
        Set<Method> methodsToDocument = getMethodsToDocument();
        for (Method method : methodsToDocument) {
            addReturnType(candidates, method);
            addBodyParam(candidates, method);
        }
        return candidates;
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

    private void addReturnType(Set<Class<?>> candidates, Method method) {
        Class<?> returnValueClass = method.getReturnType();
        if (returnValueClass.isPrimitive() || returnValueClass.equals(Livedoc.class)) {
            return;
        }
        candidates.addAll(getTypesToDocument(method.getGenericReturnType()));
    }

    private void addBodyParam(Set<Class<?>> candidates, Method method) {
        int bodyParamIndex = SpringRequestBodyBuilder.getIndexOfBodyParam(method);
        if (bodyParamIndex >= 0) {
            Type bodyParamType = method.getGenericParameterTypes()[bodyParamIndex];
            candidates.addAll(getTypesToDocument(bodyParamType));
        }
    }

    private boolean inWhiteListedPackages(List<String> packages, Class<?> clazz) {
        Package p = clazz.getPackage();
        return p != null && packages.stream().anyMatch(whiteListedPkg -> p.getName().startsWith(whiteListedPkg));
    }

    private void appendSubCandidates(Class<?> clazz, Set<Class<?>> subCandidates) {
        if (clazz.isPrimitive() || clazz.equals(Class.class)) {
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (!isValidForRecursion(field)) {
                continue;
            }

            Set<Class<?>> fieldCandidates = getTypesToDocument(field.getGenericType());

            for (Class<?> candidate : fieldCandidates) {
                if (!subCandidates.contains(candidate)) {
                    subCandidates.add(candidate);

                    appendSubCandidates(candidate, subCandidates);
                }
            }
        }
    }

    private Set<Class<?>> getTypesToDocument(Type typeDeclaration) {
        Set<Class<?>> typesInDeclaration = GenericTypeExplorer.getTypesInDeclaration(typeDeclaration);
        Set<Class<?>> types = new HashSet<>(typesInDeclaration);
        types.removeIf(ObjectsScanner::shouldIgnore);

        Set<Class<?>> interfaces = types.stream().filter(Class::isInterface).collect(Collectors.toSet());
        types.removeAll(interfaces);
        types.addAll(interfaces.stream().flatMap(this::getSubTypes).collect(Collectors.toSet()));
        return types;
    }

    private static boolean shouldIgnore(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        }
        for (Class<?> ignored : IGNORED_PARENT_TYPES) {
            if (ignored.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    private <T> Stream<Class<? extends T>> getSubTypes(Class<T> interfaceType) {
        return reflections.getSubTypesOf(interfaceType).stream();
    }

    private static boolean isValidForRecursion(Field field) {
        //        return !field.isSynthetic() && !field.getType().isPrimitive() && !Modifier.isTransient(field
        // .getModifiers());
        return true;
    }

}
