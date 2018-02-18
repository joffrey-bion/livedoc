package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;

import org.hildan.livedoc.core.annotations.ApiMethod;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
import org.hildan.livedoc.core.model.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.core.model.LivedocDefaultType;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;

public class ApiMethodDocReader {

    public static ApiMethodDoc read(Method method, ApiDoc parentApiDoc, TemplateProvider templateProvider) {
        ApiMethodDoc apiMethodDoc = new ApiMethodDoc();
        apiMethodDoc.setName(method.getName());
        apiMethodDoc.setApiErrors(ApiErrorDocReader.build(method));
        apiMethodDoc.setSupportedVersions(ApiVersionDocReader.read(method, parentApiDoc.getSupportedVersions()));
        apiMethodDoc.setAuth(ApiAuthDocReader.read(method));
        apiMethodDoc.setHeaders(ApiHeaderDocReader.read(method));
        apiMethodDoc.setPathParameters(ApiPathParamDocReader.read(method));
        apiMethodDoc.setQueryParameters(ApiQueryParamDocReader.read(method));
        apiMethodDoc.setRequestBody(ApiBodyObjectDocReader.read(method, templateProvider));
        apiMethodDoc.setResponseBodyType(readResponseBodyType(method));
        apiMethodDoc.setStage(ApiStageReader.read(method, parentApiDoc.getStage()));
        apiMethodDoc.setVisibility(ApiVisibilityReader.read(method, parentApiDoc.getVisibility()));

        ApiMethod methodAnnotation = method.getAnnotation(ApiMethod.class);
        if (methodAnnotation != null) {
            apiMethodDoc.setId(methodAnnotation.id());
            apiMethodDoc.setPaths(new LinkedHashSet<>(Arrays.asList(methodAnnotation.path())));
            apiMethodDoc.setVerbs(new LinkedHashSet<>(Arrays.asList(methodAnnotation.verbs())));
            apiMethodDoc.setSummary(methodAnnotation.summary());
            apiMethodDoc.setDescription(methodAnnotation.description());
            apiMethodDoc.setConsumes(new LinkedHashSet<>(Arrays.asList(methodAnnotation.consumes())));
            apiMethodDoc.setProduces(new LinkedHashSet<>(Arrays.asList(methodAnnotation.produces())));
            apiMethodDoc.setResponseStatusCode(methodAnnotation.responseStatusCode());
        }

        return apiMethodDoc;
    }

    private static LivedocType readResponseBodyType(Method method) {
        if (method.isAnnotationPresent(ApiResponseBodyType.class)) {
            ApiResponseBodyType annotation = method.getAnnotation(ApiResponseBodyType.class);

            if (annotation.value().isAssignableFrom(LivedocDefaultType.class)) {
                return LivedocTypeBuilder.build(method.getGenericReturnType());
            } else {
                return LivedocTypeBuilder.build(annotation.value());
            }
        }
        return null;
    }
}
