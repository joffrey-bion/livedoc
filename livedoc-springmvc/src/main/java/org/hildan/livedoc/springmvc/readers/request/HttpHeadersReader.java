package org.hildan.livedoc.springmvc.readers.request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hildan.livedoc.core.model.doc.headers.HeaderDoc;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ValueConstants;

class HttpHeadersReader {

    /**
     * Reads the headers that are necessary to resolve to the given method.
     *
     * @param method
     *         the method to read the headers from
     * @param controller
     *         the controller to read type-level headers from
     *
     * @return the {@link HeaderDoc}s for the given method
     */
    static List<HeaderDoc> buildHeadersDoc(Method method, Class<?> controller) {
        Set<HeaderDoc> headers = new HashSet<>(extractHeadersFromParams(method));

        RequestMapping methodAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if (methodAnnotation != null) {
            headers.addAll(extractHeaders(methodAnnotation));
        }

        RequestMapping typeAnnotation = AnnotatedElementUtils.findMergedAnnotation(controller, RequestMapping.class);
        if (typeAnnotation != null) {
            headers.addAll(extractHeaders(typeAnnotation));
        }

        return new ArrayList<>(headers);
    }

    private static List<HeaderDoc> extractHeaders(RequestMapping annotation) {
        List<HeaderDoc> headers = new ArrayList<>();
        for (String header : annotation.headers()) {
            headers.add(createHeaderDoc(header));
        }
        return headers;
    }

    private static HeaderDoc createHeaderDoc(String header) {
        if (header.startsWith("!")) {
            return HeaderDoc.forbidden(header.substring(1), "");
        }
        if (header.contains("!=")) {
            String[] splitHeader = header.split("!=");
            String forbiddenValue = splitHeader[1];
            return HeaderDoc.differentFrom(splitHeader[0], "", forbiddenValue);
        }
        if (header.contains("=")) {
            String[] splitHeader = header.split("=");
            String format = splitHeader[1];
            return HeaderDoc.matching(splitHeader[0], "", format);
        }
        return HeaderDoc.required(header, "");
    }

    private static Set<HeaderDoc> extractHeadersFromParams(Method method) {
        Set<HeaderDoc> headers = new LinkedHashSet<>();
        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (Annotation[] paramAnnotations : parametersAnnotations) {
            for (Annotation paramAnnotation : paramAnnotations) {
                if (paramAnnotation instanceof RequestHeader) {
                    RequestHeader requestHeader = (RequestHeader) paramAnnotation;
                    headers.add(createHeaderDoc(requestHeader));
                }
            }
        }
        return headers;
    }

    private static HeaderDoc createHeaderDoc(RequestHeader annotation) {
        String headerName = annotation.value().isEmpty() ? annotation.name() : annotation.value();
        String defaultVal = extractDefaultValue(annotation);
        if (defaultVal != null || !annotation.required()) {
            return HeaderDoc.optional(headerName, "", defaultVal);
        }
        return HeaderDoc.required(headerName, "");
    }

    private static String extractDefaultValue(RequestHeader requestHeader) {
        String defaultVal = requestHeader.defaultValue();
        if (defaultVal.equals(ValueConstants.DEFAULT_NONE)) {
            return null;
        }
        return defaultVal;
    }
}
