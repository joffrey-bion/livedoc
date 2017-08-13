package org.hildan.livedoc.springmvc.scanner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.annotation.ApiMethod;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.JSONDoc;
import org.hildan.livedoc.core.pojo.JSONDocTemplate;
import org.hildan.livedoc.core.scanner.builder.JSONDocApiMethodDocBuilder;
import org.hildan.livedoc.core.util.JSONDocUtils;
import org.hildan.livedoc.springmvc.scanner.builder.SpringConsumesBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringHeaderBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringPathBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringPathVariableBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringProducesBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringQueryParamBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringRequestBodyBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringResponseBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringResponseStatusBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringVerbBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Sets;

public class Spring4JSONDocScanner extends AbstractSpringJSONDocScanner {

    @Override
    public Set<Class<?>> jsondocControllers() {
        Set<Class<?>> jsondocControllers = reflections.getTypesAnnotatedWith(Controller.class, true);
        jsondocControllers.addAll(reflections.getTypesAnnotatedWith(RestController.class, true));

        try {
            Class.forName("org.springframework.data.rest.webmvc.RepositoryRestController");
            jsondocControllers.addAll(reflections.getTypesAnnotatedWith(RepositoryRestController.class, true));

        } catch (ClassNotFoundException e) {
            log.debug(e.getMessage() + ".class not found");
        }

        return jsondocControllers;
    }

    @Override
    public Set<Method> jsondocMethods(Class<?> controller) {
        Set<Method> annotatedMethods = new LinkedHashSet<Method>();
        for (Method method : controller.getDeclaredMethods()) {
            if (shouldDocument(method)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    private boolean shouldDocument(Method method) {
        return method.isAnnotationPresent(RequestMapping.class) || method.isAnnotationPresent(MessageMapping.class)
                || method.isAnnotationPresent(SubscribeMapping.class);
    }

    @Override
    public ApiMethodDoc initApiMethodDoc(Method method, Map<Class<?>, JSONDocTemplate> jsondocTemplates) {
        ApiMethodDoc apiMethodDoc = new ApiMethodDoc();
        apiMethodDoc.setPath(SpringPathBuilder.buildPath(method));
        apiMethodDoc.setMethod(method.getName());
        apiMethodDoc.setVerb(SpringVerbBuilder.buildVerb(method));
        apiMethodDoc.setProduces(SpringProducesBuilder.buildProduces(method));
        apiMethodDoc.setConsumes(SpringConsumesBuilder.buildConsumes(method));
        apiMethodDoc.setHeaders(SpringHeaderBuilder.buildHeaders(method));
        apiMethodDoc.setPathparameters(SpringPathVariableBuilder.buildPathVariable(method));
        apiMethodDoc.setQueryparameters(SpringQueryParamBuilder.buildQueryParams(method));
        apiMethodDoc.setBodyobject(SpringRequestBodyBuilder.buildRequestBody(method));
        apiMethodDoc.setResponse(SpringResponseBuilder.buildResponse(method));
        apiMethodDoc.setResponsestatuscode(SpringResponseStatusBuilder.buildResponseStatusCode(method));

        Integer index = JSONDocUtils.getIndexOfParameterWithAnnotation(method, RequestBody.class);
        if (index != -1) {
            apiMethodDoc.getBodyobject().setJsondocTemplate(jsondocTemplates.get(method.getParameterTypes()[index]));
        }

        return apiMethodDoc;
    }

    @Override
    public ApiMethodDoc mergeApiMethodDoc(Method method, ApiMethodDoc apiMethodDoc) {
        if (method.isAnnotationPresent(ApiMethod.class) && method.getDeclaringClass().isAnnotationPresent(Api.class)) {
            ApiMethodDoc jsondocApiMethodDoc = JSONDocApiMethodDocBuilder.build(method);
            BeanUtils.copyProperties(jsondocApiMethodDoc, apiMethodDoc, "path", "verb", "produces", "consumes",
                    "headers", "pathparameters", "queryparameters", "bodyobject", "response", "responsestatuscode",
                    "apierrors", "supportedversions", "auth", "displayMethodAs");
        }
        return apiMethodDoc;
    }

    @Override
    public Set<Class<?>> jsondocObjects(List<String> packages) {
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

    private void addReturnType(Set<Class<?>> candidates, Method method) {
        Class<?> returnValueClass = method.getReturnType();
        if (returnValueClass.isPrimitive() || returnValueClass.equals(JSONDoc.class)) {
            return;
        }
        buildJSONDocObjectsCandidates(candidates, returnValueClass, method.getGenericReturnType(),
                reflections);
    }

    private void addBodyParam(Set<Class<?>> candidates, Method method) {
        int bodyParamIndex = SpringRequestBodyBuilder.getIndexOfBodyParam(method);
        if (bodyParamIndex >= 0) {
            Class<?> bodyParamClass = method.getParameterTypes()[bodyParamIndex];
            Type bodyParamType = method.getGenericParameterTypes()[bodyParamIndex];
            buildJSONDocObjectsCandidates(candidates, bodyParamClass, bodyParamType, reflections);
        }
    }

    private Set<Method> getMethodsToDocument() {
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(RequestMapping.class);
        methodsAnnotatedWith.addAll(reflections.getMethodsAnnotatedWith(SubscribeMapping.class));
        methodsAnnotatedWith.addAll(reflections.getMethodsAnnotatedWith(MessageMapping.class));
        return methodsAnnotatedWith;
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

            Class<?> fieldClass = field.getType();
            Set<Class<?>> fieldCandidates = new HashSet<>();
            buildJSONDocObjectsCandidates(fieldCandidates, fieldClass, field.getGenericType(), reflections);

            for (Class<?> candidate : fieldCandidates) {
                if (!subCandidates.contains(candidate)) {
                    subCandidates.add(candidate);

                    appendSubCandidates(candidate, subCandidates);
                }
            }
        }
    }

    private static boolean isValidForRecursion(Field field) {
        return !field.isSynthetic() && !field.getType().isPrimitive() && !Modifier.isTransient(field.getModifiers());
    }
}
