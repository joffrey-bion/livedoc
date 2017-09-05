package org.hildan.livedoc.core.builders.doc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hildan.livedoc.core.annotations.ApiParams;
import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.builders.types.LivedocType;
import org.hildan.livedoc.core.builders.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.pojo.ApiParamDoc;

public class ApiQueryParamDocReader {

    public static Set<ApiParamDoc> read(Method method) {
        Set<ApiParamDoc> docs = new LinkedHashSet<>();

        if (method.isAnnotationPresent(ApiParams.class)) {
            for (ApiQueryParam apiParam : method.getAnnotation(ApiParams.class).queryparams()) {
                ApiParamDoc apiParamDoc = buildFromAnnotation(apiParam,
                        LivedocTypeBuilder.build(apiParam.clazz(), apiParam.clazz()));
                docs.add(apiParamDoc);
            }
        }

        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parametersAnnotations.length; i++) {
            for (int j = 0; j < parametersAnnotations[i].length; j++) {
                if (parametersAnnotations[i][j] instanceof ApiQueryParam) {
                    ApiQueryParam annotation = (ApiQueryParam) parametersAnnotations[i][j];
                    ApiParamDoc apiParamDoc = buildFromAnnotation(annotation,
                            LivedocTypeBuilder.build(method.getParameterTypes()[i],
                                    method.getGenericParameterTypes()[i]));
                    docs.add(apiParamDoc);
                }
            }
        }

        return docs;
    }

    public static ApiParamDoc buildFromAnnotation(ApiQueryParam annotation, LivedocType livedocType) {
        return new ApiParamDoc(annotation.name(), annotation.description(), livedocType,
                String.valueOf(annotation.required()), annotation.allowedvalues(), annotation.format(),
                annotation.defaultvalue());
    }
}
