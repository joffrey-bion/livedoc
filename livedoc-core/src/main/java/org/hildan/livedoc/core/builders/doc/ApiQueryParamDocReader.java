package org.hildan.livedoc.core.builders.doc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hildan.livedoc.core.annotations.ApiParams;
import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.model.doc.ApiParamDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;

public class ApiQueryParamDocReader {

    public static Set<ApiParamDoc> read(Method method, TypeReferenceProvider typeReferenceProvider) {
        Set<ApiParamDoc> docs = new LinkedHashSet<>();

        if (method.isAnnotationPresent(ApiParams.class)) {
            for (ApiQueryParam apiParam : method.getAnnotation(ApiParams.class).queryParams()) {
                LivedocType livedocType = typeReferenceProvider.getReference(apiParam.type());
                ApiParamDoc apiParamDoc = buildFromAnnotation(apiParam, livedocType);
                docs.add(apiParamDoc);
            }
        }

        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parametersAnnotations.length; i++) {
            for (int j = 0; j < parametersAnnotations[i].length; j++) {
                if (parametersAnnotations[i][j] instanceof ApiQueryParam) {
                    ApiQueryParam annotation = (ApiQueryParam) parametersAnnotations[i][j];
                    LivedocType livedocType = typeReferenceProvider.getReference(method.getGenericParameterTypes()[i]);
                    ApiParamDoc apiParamDoc = buildFromAnnotation(annotation, livedocType);
                    docs.add(apiParamDoc);
                }
            }
        }

        return docs;
    }

    private static ApiParamDoc buildFromAnnotation(ApiQueryParam annotation, LivedocType livedocType) {
        return new ApiParamDoc(annotation.name(), annotation.description(), livedocType,
                String.valueOf(annotation.required()), annotation.allowedValues(), annotation.format(),
                annotation.defaultValue());
    }
}
