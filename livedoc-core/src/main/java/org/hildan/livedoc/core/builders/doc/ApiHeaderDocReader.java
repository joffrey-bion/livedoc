package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hildan.livedoc.core.annotations.ApiHeader;
import org.hildan.livedoc.core.annotations.ApiHeaders;
import org.hildan.livedoc.core.model.doc.ApiHeaderDoc;

public class ApiHeaderDocReader {

    public static Set<ApiHeaderDoc> read(Method method) {
        Set<ApiHeaderDoc> docs = new LinkedHashSet<>();

        ApiHeaders methodAnnotation = method.getAnnotation(ApiHeaders.class);
        ApiHeaders typeAnnotation = method.getDeclaringClass().getAnnotation(ApiHeaders.class);

        if (typeAnnotation != null) {
            for (ApiHeader apiHeader : typeAnnotation.headers()) {
                docs.add(new ApiHeaderDoc(apiHeader.name(), apiHeader.description(), apiHeader.allowedValues()));
            }
        }

        if (methodAnnotation != null) {
            for (ApiHeader apiHeader : methodAnnotation.headers()) {
                docs.add(new ApiHeaderDoc(apiHeader.name(), apiHeader.description(), apiHeader.allowedValues()));
            }
        }

        return docs;
    }

}
