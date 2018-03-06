package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.model.doc.ApiParamDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.springmvc.scanner.utils.JavadocHelper;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.web.bind.annotation.PathVariable;

public class SpringPathVariableBuilder {

    private static final DefaultParameterNameDiscoverer PARAM_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    public static Set<ApiParamDoc> buildPathVariable(Method method, TypeReferenceProvider typeReferenceProvider) {
        Set<ApiParamDoc> apiParamDocs = new LinkedHashSet<>();

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            PathVariable pathVariable = param.getAnnotation(PathVariable.class);
            ApiPathParam apiPathParam = param.getAnnotation(ApiPathParam.class);
            if (pathVariable != null) {
                LivedocType livedocType = typeReferenceProvider.getReference(param.getParameterizedType());
                String paramName = getSpringParamName(method, pathVariable, i);
                String description = JavadocHelper.getJavadocDescription(method, paramName).orElse("");
                ApiParamDoc apiParamDoc = new ApiParamDoc(paramName, description, livedocType, "true", new String[0],
                        null, "");

                if (apiPathParam != null) {
                    mergeApiPathParamDoc(apiPathParam, apiParamDoc);
                }
                apiParamDocs.add(apiParamDoc);
            }
        }

        return apiParamDocs;
    }

    private static String getSpringParamName(Method method, PathVariable pathVariable, int index) {
        if (!pathVariable.value().isEmpty()) {
            return pathVariable.value();
        }
        return PARAM_NAME_DISCOVERER.getParameterNames(method)[index];
    }

    /**
     * Available properties that can be overridden: name, description, allowedValues, format. Name is overridden only if
     * it's empty in the apiParamDoc argument. Description, format and allowedValues are copied in any case.
     *
     * @param apiPathParam
     * @param apiParamDoc
     */
    private static void mergeApiPathParamDoc(ApiPathParam apiPathParam, ApiParamDoc apiParamDoc) {
        if (apiParamDoc.getName().trim().isEmpty()) {
            apiParamDoc.setName(apiPathParam.name());
        }
        if (apiParamDoc.getDescription().trim().isEmpty()) {
            apiParamDoc.setDescription(apiPathParam.name());
        }
        apiParamDoc.setAllowedValues(apiPathParam.allowedValues());
        apiParamDoc.setFormat(apiPathParam.format());
    }

}
