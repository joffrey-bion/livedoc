package org.hildan.livedoc.core.scanner.readers;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;

import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.annotation.ApiMethod;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiStage;
import org.hildan.livedoc.core.pojo.ApiVerb;
import org.hildan.livedoc.core.pojo.ApiVisibility;

public class ApiMethodDocReader {

    public static ApiMethodDoc read(Method method) {
        ApiMethod methodAnnotation = method.getAnnotation(ApiMethod.class);
        Api controllerAnnotation = method.getDeclaringClass().getAnnotation(Api.class);

        ApiMethodDoc apiMethodDoc = new ApiMethodDoc();
        apiMethodDoc.setId(methodAnnotation.id());
        apiMethodDoc.setPath(new LinkedHashSet<>(Arrays.asList(methodAnnotation.path())));
        apiMethodDoc.setMethod(method.getName());
        apiMethodDoc.setSummary(methodAnnotation.summary());
        apiMethodDoc.setDescription(methodAnnotation.description());
        apiMethodDoc.setVerb(new LinkedHashSet<>(Arrays.asList(methodAnnotation.verb())));
        apiMethodDoc.setConsumes(new LinkedHashSet<>(Arrays.asList(methodAnnotation.consumes())));
        apiMethodDoc.setProduces(new LinkedHashSet<>(Arrays.asList(methodAnnotation.produces())));
        apiMethodDoc.setResponsestatuscode(methodAnnotation.responsestatuscode());

        if (methodAnnotation.visibility().equals(ApiVisibility.UNDEFINED)) {
            apiMethodDoc.setVisibility(controllerAnnotation.visibility());
        } else {
            apiMethodDoc.setVisibility(methodAnnotation.visibility());
        }

        if (methodAnnotation.stage().equals(ApiStage.UNDEFINED)) {
            apiMethodDoc.setStage(controllerAnnotation.stage());
        } else {
            apiMethodDoc.setStage(methodAnnotation.stage());
        }

        apiMethodDoc.setHeaders(ApiHeaderDocReader.read(method));
        apiMethodDoc.setPathparameters(ApiPathParameterDocReader.read(method));
        apiMethodDoc.setQueryparameters(ApiQueryParameterDocReader.read(method));
        apiMethodDoc.setBodyobject(ApiBodyObjectDocReader.read(method));
        apiMethodDoc.setResponse(ApiResponseObjectDocReader.build(method));

        return apiMethodDoc;
    }

}
