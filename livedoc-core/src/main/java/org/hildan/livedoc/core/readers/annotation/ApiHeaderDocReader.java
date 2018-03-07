package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hildan.livedoc.core.annotations.ApiHeader;
import org.hildan.livedoc.core.annotations.ApiHeaders;
import org.hildan.livedoc.core.model.doc.headers.ApiHeaderDoc;

public class ApiHeaderDocReader {

    public static List<ApiHeaderDoc> read(Method method) {
        Set<ApiHeaderDoc> docs = new LinkedHashSet<>();

        ApiHeaders typeAnnotation = method.getDeclaringClass().getAnnotation(ApiHeaders.class);
        if (typeAnnotation != null) {
            docs.addAll(extractHeaders(typeAnnotation));
        }

        ApiHeaders methodAnnotation = method.getAnnotation(ApiHeaders.class);
        if (methodAnnotation != null) {
            docs.addAll(extractHeaders(methodAnnotation));
        }

        return new ArrayList<>(docs);
    }

    private static Set<ApiHeaderDoc> extractHeaders(ApiHeaders annotation) {
        Set<ApiHeaderDoc> headers = new HashSet<>();
        for (ApiHeader header : annotation.headers()) {
            List<String> allowedValues = Arrays.asList(header.allowedValues());
            ApiHeaderDoc headerDoc = ApiHeaderDoc.oneOf(header.name(), header.description(), allowedValues);
            headers.add(headerDoc);
        }
        return headers;
    }
}
