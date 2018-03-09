package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.hildan.livedoc.springmvc.scanner.utils.ClasspathUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class SpringVerbBuilder {

    private static final List<RequestMethod> DEFAULT_METHODS = Arrays.asList(RequestMethod.values());

    /**
     * Reads {@link ApiVerb}s (HTTP methods) from the given API method.
     *
     * @param method
     *         the method to read the verbs from
     * @param controller
     *         the controller to get type-level verbs from
     *
     * @return the {@link ApiVerb}s (HTTP methods) of the given API method.
     */
    public static List<ApiVerb> buildVerb(Method method, Class<?> controller) {
        List<RequestMethod> methods = new ArrayList<>(DEFAULT_METHODS);

        List<RequestMethod> methodLevelMethods = getMethods(method);
        if (!methodLevelMethods.isEmpty()) {
            methods.retainAll(methodLevelMethods);
        }

        List<RequestMethod> typeLevelMethods = getMethods(controller);
        if (!typeLevelMethods.isEmpty()) {
            methods.retainAll(typeLevelMethods);
        }

        return methods.stream().map(SpringVerbBuilder::toApiVerb).distinct().sorted().collect(Collectors.toList());
    }

    private static List<RequestMethod> getMethods(AnnotatedElement element) {
        RequestMapping requestMapping = element.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            return Arrays.asList(requestMapping.method());
        }
        if (ClasspathUtils.isGetMappingOnClasspath()) {
            if (element.isAnnotationPresent(GetMapping.class)) {
                return Collections.singletonList(RequestMethod.GET);
            }
            if (element.isAnnotationPresent(PostMapping.class)) {
                return Collections.singletonList(RequestMethod.POST);
            }
            if (element.isAnnotationPresent(PutMapping.class)) {
                return Collections.singletonList(RequestMethod.PUT);
            }
            if (element.isAnnotationPresent(DeleteMapping.class)) {
                return Collections.singletonList(RequestMethod.DELETE);
            }
        }
        return Collections.emptyList();
    }

    private static ApiVerb toApiVerb(RequestMethod requestMethod) {
        return ApiVerb.valueOf(requestMethod.name());
    }
}
