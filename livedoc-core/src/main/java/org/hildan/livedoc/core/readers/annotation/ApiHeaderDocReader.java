package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.ApiHeader;
import org.hildan.livedoc.core.annotations.ApiHeaders;
import org.hildan.livedoc.core.model.doc.headers.HeaderDoc;
import org.jetbrains.annotations.NotNull;

public class ApiHeaderDocReader {

    public static List<HeaderDoc> read(Method method) {
        Set<HeaderDoc> docs = new LinkedHashSet<>();

        ApiHeaders typeAnnotation = method.getDeclaringClass().getAnnotation(ApiHeaders.class);
        if (typeAnnotation != null) {
            docs.addAll(buildHeaderDocs(typeAnnotation.headers()));
        }

        ApiHeaders methodAnnotation = method.getAnnotation(ApiHeaders.class);
        if (methodAnnotation != null) {
            docs.addAll(buildHeaderDocs(methodAnnotation.headers()));
        }

        return new ArrayList<>(docs);
    }

    @NotNull
    public static List<HeaderDoc> buildHeaderDocs(ApiHeader[] headerAnnotations) {
        return Arrays.stream(headerAnnotations)
                     .map(ApiHeaderDocReader::buildHeaderDoc)
                     .collect(Collectors.toList());
    }

    @NotNull
    private static HeaderDoc buildHeaderDoc(ApiHeader header) {
        List<String> allowedValues = Arrays.asList(header.allowedValues());
        return HeaderDoc.oneOf(header.name(), header.description(), allowedValues);
    }
}
