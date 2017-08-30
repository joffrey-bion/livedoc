package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;

import org.hildan.livedoc.core.annotations.ApiMethod;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiStage;
import org.hildan.livedoc.core.pojo.ApiVisibility;

public class ApiMethodDocReader {

    public static ApiMethodDoc read(Method method, ApiDoc parentApiDoc) {

        ApiMethodDoc apiMethodDoc = new ApiMethodDoc();
        apiMethodDoc.setMethod(method.getName());
        apiMethodDoc.setApierrors(ApiErrorDocReader.build(method));
        apiMethodDoc.setSupportedversions(ApiVersionDocReader.read(method, parentApiDoc.getSupportedversions()));
        apiMethodDoc.setAuth(ApiAuthDocReader.read(method));
        apiMethodDoc.setHeaders(ApiHeaderDocReader.read(method));
        apiMethodDoc.setPathparameters(ApiPathParameterDocReader.read(method));
        apiMethodDoc.setQueryparameters(ApiQueryParameterDocReader.read(method));
        apiMethodDoc.setBodyobject(ApiBodyObjectDocReader.read(method));
        apiMethodDoc.setResponse(ApiResponseObjectDocReader.build(method));
        apiMethodDoc.setVisibility(parentApiDoc.getVisibility());
        apiMethodDoc.setStage(parentApiDoc.getStage());

        ApiMethod methodAnnotation = method.getAnnotation(ApiMethod.class);
        if (methodAnnotation != null) {
            apiMethodDoc.setId(methodAnnotation.id());
            apiMethodDoc.setPath(new LinkedHashSet<>(Arrays.asList(methodAnnotation.path())));
            apiMethodDoc.setSummary(methodAnnotation.summary());
            apiMethodDoc.setDescription(methodAnnotation.description());
            apiMethodDoc.setVerb(new LinkedHashSet<>(Arrays.asList(methodAnnotation.verb())));
            apiMethodDoc.setConsumes(new LinkedHashSet<>(Arrays.asList(methodAnnotation.consumes())));
            apiMethodDoc.setProduces(new LinkedHashSet<>(Arrays.asList(methodAnnotation.produces())));
            apiMethodDoc.setResponsestatuscode(methodAnnotation.responsestatuscode());
            if (!methodAnnotation.visibility().equals(ApiVisibility.UNDEFINED)) {
                apiMethodDoc.setVisibility(methodAnnotation.visibility());
            }
            if (!methodAnnotation.stage().equals(ApiStage.UNDEFINED)) {
                apiMethodDoc.setStage(methodAnnotation.stage());
            }
        }

        return apiMethodDoc;
    }

}
