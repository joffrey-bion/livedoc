package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
import org.hildan.livedoc.core.model.LivedocDefaultType;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

public class ApiOperationDocReader {

    public static ApiOperationDoc read(Method method, ApiDoc parentApiDoc, TypeReferenceProvider typeReferenceProvider,
            TemplateProvider templateProvider) {
        ApiOperationDoc apiOperationDoc = new ApiOperationDoc();
        apiOperationDoc.setName(method.getName());
        apiOperationDoc.setApiErrors(ApiErrorDocReader.build(method));
        apiOperationDoc.setSupportedVersions(ApiVersionDocReader.read(method, parentApiDoc.getSupportedVersions()));
        apiOperationDoc.setAuth(ApiAuthDocReader.readMethod(method));
        apiOperationDoc.setHeaders(ApiHeaderDocReader.read(method));
        apiOperationDoc.setPathParameters(ApiPathParamDocReader.read(method, typeReferenceProvider));
        apiOperationDoc.setQueryParameters(ApiQueryParamDocReader.read(method, typeReferenceProvider));
        apiOperationDoc.setRequestBody(
                ApiRequestBodyTypeDocReader.read(method, typeReferenceProvider, templateProvider));
        apiOperationDoc.setResponseBodyType(readResponseBodyType(method, typeReferenceProvider));
        apiOperationDoc.setStage(ApiStageReader.read(method, parentApiDoc.getStage()));

        ApiOperation methodAnnotation = method.getAnnotation(ApiOperation.class);
        if (methodAnnotation != null) {
            String overriddenId = nullifyIfEmpty(methodAnnotation.id());
            if (overriddenId != null) {
                apiOperationDoc.setLivedocId(overriddenId);
            }
            apiOperationDoc.setPaths(Arrays.asList(methodAnnotation.path()));
            apiOperationDoc.setVerbs(Arrays.asList(methodAnnotation.verbs()));
            apiOperationDoc.setSummary(nullifyIfEmpty(methodAnnotation.summary()));
            apiOperationDoc.setDescription(nullifyIfEmpty(methodAnnotation.description()));
            apiOperationDoc.setConsumes(Arrays.asList(methodAnnotation.consumes()));
            apiOperationDoc.setProduces(Arrays.asList(methodAnnotation.produces()));
            apiOperationDoc.setResponseStatusCode(methodAnnotation.responseStatusCode());
        }
        return apiOperationDoc;
    }

    @Nullable
    private static LivedocType readResponseBodyType(Method method, TypeReferenceProvider typeReferenceProvider) {
        ApiResponseBodyType annotation = method.getAnnotation(ApiResponseBodyType.class);
        if (annotation == null) {
            return null;
        }
        Type type = extractResponseBodyType(method, annotation);
        return typeReferenceProvider.getReference(type);
    }

    @NotNull
    private static Type extractResponseBodyType(@NotNull Method method, @NotNull ApiResponseBodyType annotation) {
        Class<?> type = annotation.value();
        if (type.equals(LivedocDefaultType.class)) {
            return method.getGenericReturnType();
        }
        return type;
    }
}
