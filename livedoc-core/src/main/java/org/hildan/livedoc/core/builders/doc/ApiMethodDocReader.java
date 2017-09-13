package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;

import org.hildan.livedoc.core.annotations.ApiMethod;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiStage;
import org.hildan.livedoc.core.pojo.ApiVisibility;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;

public class ApiMethodDocReader {

    public static ApiMethodDoc read(Method method, ApiDoc parentApiDoc, TemplateProvider templateProvider) {
        ApiMethod methodAnnotation = method.getAnnotation(ApiMethod.class);
        ApiMethodDoc apiMethodDoc = new ApiMethodDoc();
        apiMethodDoc.setId(methodAnnotation.id());
        apiMethodDoc.setMethod(method.getName());
        apiMethodDoc.setPath(new LinkedHashSet<>(Arrays.asList(methodAnnotation.path())));
        apiMethodDoc.setVerb(new LinkedHashSet<>(Arrays.asList(methodAnnotation.verb())));
        apiMethodDoc.setSummary(methodAnnotation.summary());
        apiMethodDoc.setDescription(methodAnnotation.description());
        apiMethodDoc.setApierrors(ApiErrorDocReader.build(method));
        apiMethodDoc.setSupportedversions(ApiVersionDocReader.read(method, parentApiDoc.getSupportedversions()));
        apiMethodDoc.setAuth(ApiAuthDocReader.read(method));
        apiMethodDoc.setHeaders(ApiHeaderDocReader.read(method));
        apiMethodDoc.setPathparameters(ApiPathParamDocReader.read(method));
        apiMethodDoc.setQueryparameters(ApiQueryParamDocReader.read(method));
        apiMethodDoc.setBodyobject(ApiBodyObjectDocReader.read(method, templateProvider));
        apiMethodDoc.setResponse(ApiResponseObjectDocReader.build(method));
        apiMethodDoc.setConsumes(new LinkedHashSet<>(Arrays.asList(methodAnnotation.consumes())));
        apiMethodDoc.setProduces(new LinkedHashSet<>(Arrays.asList(methodAnnotation.produces())));
        apiMethodDoc.setResponsestatuscode(methodAnnotation.responsestatuscode());
        apiMethodDoc.setVisibility(getApiVisibility(parentApiDoc, methodAnnotation));
        apiMethodDoc.setStage(getApiStage(parentApiDoc, methodAnnotation));
        return apiMethodDoc;
    }

    private static ApiVisibility getApiVisibility(ApiDoc parentApiDoc, ApiMethod methodAnnotation) {
        ApiVisibility visibility = methodAnnotation.visibility();
        if (visibility.equals(ApiVisibility.UNDEFINED)) {
           return parentApiDoc.getVisibility();
        }
        return visibility;
    }

    private static ApiStage getApiStage(ApiDoc parentApiDoc, ApiMethod methodAnnotation) {
        ApiStage stage = methodAnnotation.stage();
        if (stage.equals(ApiStage.UNDEFINED)) {
           return parentApiDoc.getStage();
        }
        return stage;
    }

}
