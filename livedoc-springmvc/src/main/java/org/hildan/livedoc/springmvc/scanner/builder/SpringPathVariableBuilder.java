package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.model.doc.ParamDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.readers.javadoc.JavadocHelper;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.web.bind.annotation.PathVariable;

public class SpringPathVariableBuilder {

    private static final DefaultParameterNameDiscoverer PARAM_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    public static List<ParamDoc> buildPathVariable(Method method, TypeReferenceProvider typeReferenceProvider) {
        List<ParamDoc> paramDocs = new ArrayList<>();

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            PathVariable pathVariable = param.getAnnotation(PathVariable.class);
            ApiPathParam apiPathParam = param.getAnnotation(ApiPathParam.class);
            if (pathVariable != null) {
                LivedocType livedocType = typeReferenceProvider.getReference(param.getParameterizedType());
                String paramName = getSpringParamName(method, pathVariable, i);
                String description = JavadocHelper.getJavadocDescription(method, paramName).orElse("");
                ParamDoc paramDoc = new ParamDoc(paramName, description, livedocType, "true", new String[0], null,
                        null);

                if (apiPathParam != null) {
                    mergeApiPathParamDoc(apiPathParam, paramDoc);
                }
                paramDocs.add(paramDoc);
            }
        }

        return paramDocs;
    }

    private static String getSpringParamName(Method method, PathVariable pathVariable, int index) {
        if (!pathVariable.value().isEmpty()) {
            return pathVariable.value();
        }
        return PARAM_NAME_DISCOVERER.getParameterNames(method)[index];
    }

    /**
     * Available properties that can be overridden: name, description, allowedValues, format. Name is overridden only if
     * it's empty in the paramDoc argument. Description, format and allowedValues are copied in any case.
     *
     * @param apiPathParam
     *         the annotation to get the new data from
     * @param paramDoc
     *         the {@link ParamDoc} object to update
     */
    private static void mergeApiPathParamDoc(ApiPathParam apiPathParam, ParamDoc paramDoc) {
        if (paramDoc.getName().trim().isEmpty()) {
            paramDoc.setName(apiPathParam.name());
        }
        if (paramDoc.getDescription().trim().isEmpty()) {
            paramDoc.setDescription(apiPathParam.description());
        }
        paramDoc.setAllowedValues(apiPathParam.allowedValues());
        paramDoc.setFormat(apiPathParam.format());
    }

}
