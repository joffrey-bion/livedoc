package org.hildan.livedoc.core.scanner.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hildan.livedoc.core.annotation.ApiParams;
import org.hildan.livedoc.core.annotation.ApiQueryParam;
import org.hildan.livedoc.core.pojo.ApiParamDoc;
import org.hildan.livedoc.core.pojo.ApiParamType;
import org.hildan.livedoc.core.util.JSONDocType;
import org.hildan.livedoc.core.util.JSONDocTypeBuilder;

public class JSONDocApiQueryParameterDocBuilder {

    public static Set<ApiParamDoc> build(Method method) {
        Set<ApiParamDoc> docs = new LinkedHashSet<ApiParamDoc>();

        if (method.isAnnotationPresent(ApiParams.class)) {
            for (ApiQueryParam apiParam : method.getAnnotation(ApiParams.class).queryparams()) {
                ApiParamDoc apiParamDoc = ApiParamDoc.buildFromAnnotation(apiParam,
                        JSONDocTypeBuilder.build(new JSONDocType(), apiParam.clazz(), apiParam.clazz()),
                        ApiParamType.QUERY);
                docs.add(apiParamDoc);
            }
        }

        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parametersAnnotations.length; i++) {
            for (int j = 0; j < parametersAnnotations[i].length; j++) {
                if (parametersAnnotations[i][j] instanceof ApiQueryParam) {
                    ApiQueryParam annotation = (ApiQueryParam) parametersAnnotations[i][j];
                    ApiParamDoc apiParamDoc = ApiParamDoc.buildFromAnnotation(annotation,
                            JSONDocTypeBuilder.build(new JSONDocType(), method.getParameterTypes()[i],
                                    method.getGenericParameterTypes()[i]), ApiParamType.QUERY);
                    docs.add(apiParamDoc);
                }
            }
        }

        return docs;
    }

}
