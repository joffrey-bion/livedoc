package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;

import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
import org.hildan.livedoc.core.model.LivedocDefaultType;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.Nullable;

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
        apiOperationDoc.setRequestBody(ApiRequestBodyDocReader.read(method, typeReferenceProvider, templateProvider));
        apiOperationDoc.setResponseBodyType(readResponseBodyType(method, typeReferenceProvider));
        apiOperationDoc.setStage(ApiStageReader.read(method, parentApiDoc.getStage()));

        ApiOperation methodAnnotation = method.getAnnotation(ApiOperation.class);
        if (methodAnnotation != null) {
            apiOperationDoc.setId(methodAnnotation.id());
            apiOperationDoc.setPaths(new LinkedHashSet<>(Arrays.asList(methodAnnotation.path())));
            apiOperationDoc.setVerbs(new LinkedHashSet<>(Arrays.asList(methodAnnotation.verbs())));
            apiOperationDoc.setSummary(methodAnnotation.summary());
            apiOperationDoc.setDescription(methodAnnotation.description());
            apiOperationDoc.setConsumes(new LinkedHashSet<>(Arrays.asList(methodAnnotation.consumes())));
            apiOperationDoc.setProduces(new LinkedHashSet<>(Arrays.asList(methodAnnotation.produces())));
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
        if (annotation.value().isAssignableFrom(LivedocDefaultType.class)) {
            return typeReferenceProvider.getReference(method.getGenericReturnType());
        } else {
            return typeReferenceProvider.getReference(annotation.value());
        }
    }
}
