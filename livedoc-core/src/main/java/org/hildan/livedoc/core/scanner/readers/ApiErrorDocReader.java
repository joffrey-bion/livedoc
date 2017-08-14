package org.hildan.livedoc.core.scanner.readers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hildan.livedoc.core.annotation.ApiError;
import org.hildan.livedoc.core.annotation.ApiErrors;
import org.hildan.livedoc.core.pojo.ApiErrorDoc;

public class ApiErrorDocReader {

    public static List<ApiErrorDoc> build(Method method) {
        List<ApiErrorDoc> apiMethodDocs = new ArrayList<>();

        ApiErrors methodAnnotation = method.getAnnotation(ApiErrors.class);
        ApiErrors typeAnnotation = method.getDeclaringClass().getAnnotation(ApiErrors.class);

        if (methodAnnotation != null) {
            for (ApiError apiError : methodAnnotation.apierrors()) {
                apiMethodDocs.add(new ApiErrorDoc(apiError.code(), apiError.description()));
            }
        }

        if (typeAnnotation != null) {
            for (final ApiError apiError : typeAnnotation.apierrors()) {

                boolean isAlreadyDefined = apiMethodDocs.stream()
                                                        .anyMatch(apiErrorDoc -> apiError.code()
                                                                                         .equals(apiErrorDoc.getCode()));
                if (!isAlreadyDefined) {
                    apiMethodDocs.add(new ApiErrorDoc(apiError.code(), apiError.description()));
                }
            }
        }
        return apiMethodDocs;
    }

}
