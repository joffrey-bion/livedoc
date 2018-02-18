package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hildan.livedoc.core.annotations.errors.ApiError;
import org.hildan.livedoc.core.annotations.errors.ApiErrors;
import org.hildan.livedoc.core.model.doc.ApiErrorDoc;

import static java.util.stream.Collectors.toList;

public class ApiErrorDocReader {

    public static List<ApiErrorDoc> build(Method method) {
        List<ApiErrorDoc> apiErrorDocs = new ArrayList<>();

        ApiErrors methodAnnotation = method.getAnnotation(ApiErrors.class);
        ApiErrors typeAnnotation = method.getDeclaringClass().getAnnotation(ApiErrors.class);

        if (methodAnnotation != null) {
            apiErrorDocs.addAll(readApiErrorDocs(methodAnnotation));
        }

        if (typeAnnotation != null) {
            List<String> alreadyUsedCodes = apiErrorDocs.stream().map(ApiErrorDoc::getCode).collect(toList());
            List<ApiErrorDoc> apiErrorDocsFromTypeAnnotation = readApiErrorDocs(typeAnnotation);
            apiErrorDocsFromTypeAnnotation.removeIf(errDoc -> alreadyUsedCodes.contains(errDoc.getCode()));
            apiErrorDocs.addAll(apiErrorDocsFromTypeAnnotation);
        }
        return apiErrorDocs;
    }

    private static List<ApiErrorDoc> readApiErrorDocs(ApiErrors annotation) {
        List<ApiErrorDoc> errorDocs = new ArrayList<>();
        for (ApiError error : annotation.value()) {
            errorDocs.add(new ApiErrorDoc(error.code(), error.description()));
        }
        return errorDocs;
    }
}
