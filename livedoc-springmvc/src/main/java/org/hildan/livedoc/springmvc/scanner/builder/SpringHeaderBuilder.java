package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.model.doc.headers.ApiHeaderDoc;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ValueConstants;

public class SpringHeaderBuilder {

    /**
     * Reads the headers that are necessary to resolve to the given method.
     *
     * @param method
     *         the method to read the headers from
     * @param controller
     *         the controller to read type-level headers from
     *
     * @return the {@link ApiHeaderDoc}s for the given method
     */
    public static Set<ApiHeaderDoc> buildHeaders(Method method, Class<?> controller) {
        Set<ApiHeaderDoc> headers = new LinkedHashSet<>();

        RequestMapping typeAnnotation = controller.getAnnotation(RequestMapping.class);
        if (typeAnnotation != null) {
            headers.addAll(extractHeaders(typeAnnotation));
        }

        RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
        if (methodAnnotation != null) {
            headers.addAll(extractHeaders(methodAnnotation));
        }

        headers.addAll(extractHeadersFromParams(method));
        return headers;
    }

    private static Set<ApiHeaderDoc> extractHeaders(RequestMapping annotation) {
        Set<ApiHeaderDoc> headers = new HashSet<>();
        for (String header : annotation.headers()) {
            headers.add(createHeaderDoc(header));
        }
        return headers;
    }

    private static ApiHeaderDoc createHeaderDoc(String header) {
        if (header.startsWith("!")) {
            return ApiHeaderDoc.forbidden(header, "");
        }
        if (header.contains("!=")) {
            String[] splitHeader = header.split("!=");
            String forbiddenValue = splitHeader[1];
            return ApiHeaderDoc.differentFrom(splitHeader[0], "", forbiddenValue);
        }
        if (header.contains("=")) {
            String[] splitHeader = header.split("=");
            String format = splitHeader[1];
            return ApiHeaderDoc.matching(splitHeader[0], "", format);
        }
        return ApiHeaderDoc.required(header, "");
    }

    private static Set<ApiHeaderDoc> extractHeadersFromParams(Method method) {
        Set<ApiHeaderDoc> headers = new LinkedHashSet<>();
        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (Annotation[] paramAnnotations : parametersAnnotations) {
            for (Annotation paramAnnotation : paramAnnotations) {
                if (paramAnnotation instanceof RequestHeader) {
                    RequestHeader requestHeader = (RequestHeader) paramAnnotation;
                    headers.add(createApiHeaderDoc(requestHeader));
                }
            }
        }
        return headers;
    }

    private static ApiHeaderDoc createApiHeaderDoc(RequestHeader annotation) {
        String headerName = annotation.value().isEmpty() ? annotation.name() : annotation.value();
        String defaultVal = extractDefaultValue(annotation);
        if (defaultVal != null || !annotation.required()) {
            return ApiHeaderDoc.optional(headerName, "", defaultVal);
        }
        return ApiHeaderDoc.required(headerName, "");
    }

    private static String extractDefaultValue(RequestHeader requestHeader) {
        String defaultVal = requestHeader.defaultValue();
        if (defaultVal.equals(ValueConstants.DEFAULT_NONE)) {
            return null;
        }
        return defaultVal;
    }
}
