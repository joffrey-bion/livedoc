package org.hildan.livedoc.core.readers.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hildan.livedoc.core.annotations.ApiParams;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.model.doc.ApiParamDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;

public class ApiPathParamDocReader {

    public static List<ApiParamDoc> read(Method method, TypeReferenceProvider typeReferenceProvider) {
        Set<ApiParamDoc> docs = new LinkedHashSet<>();

        ApiParams apiParams = method.getAnnotation(ApiParams.class);
        if (apiParams != null) {
            for (ApiPathParam apiParam : apiParams.pathParams()) {
                LivedocType type = typeReferenceProvider.getReference(apiParam.type());
                ApiParamDoc apiParamDoc = buildFromAnnotation(apiParam, type);
                docs.add(apiParamDoc);
            }
        }

        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parametersAnnotations.length; i++) {
            for (int j = 0; j < parametersAnnotations[i].length; j++) {
                if (parametersAnnotations[i][j] instanceof ApiPathParam) {
                    ApiPathParam annotation = (ApiPathParam) parametersAnnotations[i][j];
                    Type type = method.getGenericParameterTypes()[i];
                    LivedocType livedocType = typeReferenceProvider.getReference(type);
                    ApiParamDoc apiParamDoc = buildFromAnnotation(annotation, livedocType);
                    docs.add(apiParamDoc);
                }
            }
        }

        return new ArrayList<>(docs);
    }

    private static ApiParamDoc buildFromAnnotation(ApiPathParam annotation, LivedocType livedocType) {
        return new ApiParamDoc(annotation.name(), annotation.description(), livedocType, "true",
                annotation.allowedValues(), annotation.format(), null);
    }
}
