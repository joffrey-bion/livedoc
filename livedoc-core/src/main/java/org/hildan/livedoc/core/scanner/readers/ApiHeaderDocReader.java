package org.hildan.livedoc.core.scanner.readers;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hildan.livedoc.core.annotation.ApiHeader;
import org.hildan.livedoc.core.annotation.ApiHeaders;
import org.hildan.livedoc.core.pojo.ApiHeaderDoc;

public class ApiHeaderDocReader {

    public static Set<ApiHeaderDoc> read(Method method) {
        Set<ApiHeaderDoc> docs = new LinkedHashSet<ApiHeaderDoc>();

        ApiHeaders methodAnnotation = method.getAnnotation(ApiHeaders.class);
        ApiHeaders typeAnnotation = method.getDeclaringClass().getAnnotation(ApiHeaders.class);

        if (typeAnnotation != null) {
            for (ApiHeader apiHeader : typeAnnotation.headers()) {
                docs.add(new ApiHeaderDoc(apiHeader.name(), apiHeader.description(), apiHeader.allowedvalues()));
            }
        }

        if (methodAnnotation != null) {
            for (ApiHeader apiHeader : methodAnnotation.headers()) {
                docs.add(new ApiHeaderDoc(apiHeader.name(), apiHeader.description(), apiHeader.allowedvalues()));
            }
        }

        return docs;
    }

}
