package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.model.doc.ApiParamDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.springmvc.scanner.utils.JavadocHelper;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

public class SpringQueryParamBuilder {

    private static final DefaultParameterNameDiscoverer PARAM_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    public static List<ApiParamDoc> buildQueryParams(Method method, Class<?> controller,
            TypeReferenceProvider typeReferenceProvider) {
        List<ApiParamDoc> apiParamDocs = new ArrayList<>();

        RequestMapping requestMapping = controller.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            apiParamDocs.addAll(getParamDocsFromAnnotation(method, requestMapping, typeReferenceProvider));
        }

        requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            apiParamDocs.addAll(getParamDocsFromAnnotation(method, requestMapping, typeReferenceProvider));
        }

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            RequestParam requestParamAnn = param.getAnnotation(RequestParam.class);
            ModelAttribute modelAttributeAnn = param.getAnnotation(ModelAttribute.class);
            ApiQueryParam apiQueryParam = param.getAnnotation(ApiQueryParam.class);

            LivedocType paramType = typeReferenceProvider.getReference(param.getParameterizedType());
            if (requestParamAnn != null) {
                ApiParamDoc apiParamDoc = getParamDocFromRequestParam(method, i, requestParamAnn, paramType);
                mergeApiQueryParamDoc(apiQueryParam, apiParamDoc);
                apiParamDocs.add(apiParamDoc);
            }

            if (modelAttributeAnn != null) {
                String name = modelAttributeAnn.value();
                String description = JavadocHelper.getJavadocDescription(method, name).orElse("");
                ApiParamDoc apiParamDoc = new ApiParamDoc(name, description, paramType, "false", new String[] {}, null,
                        null);
                mergeApiQueryParamDoc(apiQueryParam, apiParamDoc);
                apiParamDocs.add(apiParamDoc);
            }
        }

        return apiParamDocs;
    }

    private static List<ApiParamDoc> getParamDocsFromAnnotation(Method method, RequestMapping requestMapping,
            TypeReferenceProvider typeReferenceProvider) {
        return Arrays.stream(requestMapping.params())
                     .map(param -> createParamFromSpringDefinition(method, param, typeReferenceProvider))
                     .collect(Collectors.toList());
    }

    private static ApiParamDoc createParamFromSpringDefinition(Method method, String paramStr,
            TypeReferenceProvider typeReferenceProvider) {
        String[] splitParam = paramStr.split("=");
        if (splitParam.length <= 1) {
            return createStringParamDoc(typeReferenceProvider, paramStr, method);
        }
        String name = splitParam[0];
        String value = splitParam[1];
        return createStringParamDoc(typeReferenceProvider, name, method, value);
    }

    private static ApiParamDoc createStringParamDoc(TypeReferenceProvider typeReferenceProvider, String name,
            Method method, String... values) {
        String description = JavadocHelper.getJavadocDescription(method, name).orElse("");
        LivedocType stringType = typeReferenceProvider.getReference(String.class);
        return new ApiParamDoc(name, description, stringType, "true", values, null, null);
    }

    private static ApiParamDoc getParamDocFromRequestParam(Method method, int i, RequestParam requestParamAnn,
            LivedocType paramType) {
        String name = getSpringParamName(method, requestParamAnn, i);
        String description = JavadocHelper.getJavadocDescription(method, name).orElse("");
        String required = String.valueOf(requestParamAnn.required());
        boolean hasDefault = requestParamAnn.defaultValue().equals(ValueConstants.DEFAULT_NONE);
        String defaultvalue = hasDefault ? "" : requestParamAnn.defaultValue();
        return new ApiParamDoc(name, description, paramType, required, new String[0], null, defaultvalue);
    }

    private static String getSpringParamName(Method method, RequestParam requestParam, int index) {
        if (!requestParam.name().isEmpty()) {
            return requestParam.name();
        }
        if (!requestParam.value().isEmpty()) {
            return requestParam.value();
        }
        return PARAM_NAME_DISCOVERER.getParameterNames(method)[index];
    }

    /**
     * Available properties that can be overridden: name, description, required, allowedValues, format, defaultValue.
     * Name is overridden only if it's empty in the apiParamDoc argument. Description, format and allowedValues are
     * copied in any case Default value and required are not overridden: in any case they are coming from the default
     * values of @RequestParam
     *
     * @param apiQueryParam
     *         the annotation to get the new data from
     * @param apiParamDoc
     *         the {@link ApiParamDoc} object to update
     */
    private static void mergeApiQueryParamDoc(ApiQueryParam apiQueryParam, ApiParamDoc apiParamDoc) {
        if (apiQueryParam != null) {
            if (apiParamDoc.getName().trim().isEmpty()) {
                apiParamDoc.setName(apiQueryParam.name());
            }
            if (apiParamDoc.getDescription().trim().isEmpty()) {
                apiParamDoc.setDescription(apiQueryParam.description());
            }
            apiParamDoc.setAllowedValues(apiQueryParam.allowedValues());
            apiParamDoc.setFormat(apiQueryParam.format());
        }
    }

}
