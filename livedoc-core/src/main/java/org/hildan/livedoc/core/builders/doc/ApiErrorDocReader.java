package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hildan.livedoc.core.annotations.ApiError;
import org.hildan.livedoc.core.annotations.ApiErrors;
import org.hildan.livedoc.core.pojo.ApiErrorDoc;

import static java.util.stream.Collectors.toList;

public class ApiErrorDocReader {

    public static List<ApiErrorDoc> build(Method method) {
        List<ApiErrorDoc> apiMethodDocs = new ArrayList<>();

        ApiErrors methodAnnotation = method.getAnnotation(ApiErrors.class);
        ApiErrors typeAnnotation = method.getDeclaringClass().getAnnotation(ApiErrors.class);

        if (methodAnnotation != null) {
            apiMethodDocs.addAll(readApiErrorDocs(methodAnnotation));
        }

        if (typeAnnotation != null) {
            List<String> alreadyUsedCodes = apiMethodDocs.stream().map(ApiErrorDoc::getCode).collect(toList());
            List<ApiErrorDoc> apiMethodDocs2 = readApiErrorDocs(typeAnnotation);
            apiMethodDocs2.removeIf(errDoc -> alreadyUsedCodes.contains(errDoc.getCode()));
            apiMethodDocs.addAll(apiMethodDocs2);
        }
        return apiMethodDocs;
    }

    private static List<ApiErrorDoc> readApiErrorDocs(ApiErrors annotation) {
        List<ApiErrorDoc> errorDocs = new ArrayList<>();
        for (ApiError error : annotation.apierrors()) {
            errorDocs.add(new ApiErrorDoc(error.code(), error.description()));
        }
        return errorDocs;
    }
}
