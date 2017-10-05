package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.pojo.ApiVerb;
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
    public static Set<ApiVerb> buildVerb(Method method, Class<?> controller) {
        List<RequestMethod> methods = new ArrayList<>(DEFAULT_METHODS);

        List<RequestMethod> methodLevelMethods = getMethods(method);
        if (!methodLevelMethods.isEmpty()) {
            methods.retainAll(methodLevelMethods);
        }

        List<RequestMethod> typeLevelMethods = getMethods(controller);
        if (!typeLevelMethods.isEmpty()) {
            methods.retainAll(typeLevelMethods);
        }

        return methods.stream().map(SpringVerbBuilder::toApiVerb).collect(Collectors.toSet());
    }

    private static List<RequestMethod> getMethods(AnnotatedElement element) {
        RequestMapping requestMapping = element.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(requestMapping.method());
    }

    private static ApiVerb toApiVerb(RequestMethod requestMethod) {
        return ApiVerb.valueOf(requestMethod.name());
    }
}
