package org.hildan.livedoc.core.builders.doc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hildan.livedoc.core.annotations.ApiParams;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.builders.types.LivedocType;
import org.hildan.livedoc.core.builders.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.pojo.ApiParamDoc;
import org.hildan.livedoc.core.pojo.ApiParamType;

public class ApiPathParameterDocReader {

    public static Set<ApiParamDoc> read(Method method) {
        Set<ApiParamDoc> docs = new LinkedHashSet<>();

        if (method.isAnnotationPresent(ApiParams.class)) {
            for (ApiPathParam apiParam : method.getAnnotation(ApiParams.class).pathparams()) {
                ApiParamDoc apiParamDoc = ApiParamDoc.buildFromAnnotation(apiParam,
                        LivedocTypeBuilder.build(new LivedocType(), apiParam.clazz(), apiParam.clazz()),
                        ApiParamType.PATH);
                docs.add(apiParamDoc);
            }
        }

        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parametersAnnotations.length; i++) {
            for (int j = 0; j < parametersAnnotations[i].length; j++) {
                if (parametersAnnotations[i][j] instanceof ApiPathParam) {
                    ApiPathParam annotation = (ApiPathParam) parametersAnnotations[i][j];
                    ApiParamDoc apiParamDoc = ApiParamDoc.buildFromAnnotation(annotation,
                            LivedocTypeBuilder.build(new LivedocType(), method.getParameterTypes()[i],
                                    method.getGenericParameterTypes()[i]), ApiParamType.PATH);
                    docs.add(apiParamDoc);
                }
            }
        }

        return docs;
    }

}
