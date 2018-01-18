package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.builders.types.LivedocType;
import org.hildan.livedoc.core.builders.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.pojo.ApiParamDoc;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

public class SpringQueryParamBuilder {

    private static final DefaultParameterNameDiscoverer PARAM_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * From Spring's documentation: Supported at the type level as well as at the method level! When used at the type
     * level, all method-level mappings inherit this parameter restriction
     *
     * @param method
     * @param controller
     *
     * @return
     */
    public static Set<ApiParamDoc> buildQueryParams(Method method, Class<?> controller) {
        Set<ApiParamDoc> apiParamDocs = new LinkedHashSet<>();

        RequestMapping requestMapping = controller.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            apiParamDocs.addAll(getParamDocsFromAnnotation(requestMapping));
        }

        requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            apiParamDocs.addAll(getParamDocsFromAnnotation(requestMapping));
        }

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            RequestParam requestParamAnn = param.getAnnotation(RequestParam.class);
            ModelAttribute modelAttributeAnn = param.getAnnotation(ModelAttribute.class);
            ApiQueryParam apiQueryParam = param.getAnnotation(ApiQueryParam.class);

            LivedocType paramType = LivedocTypeBuilder.build(param.getParameterizedType());
            if (requestParamAnn != null) {
                ApiParamDoc apiParamDoc = getParamDocFromRequestParam(method, i, requestParamAnn,
                        paramType);
                mergeApiQueryParamDoc(apiQueryParam, apiParamDoc);
                apiParamDocs.add(apiParamDoc);
            }

            if (modelAttributeAnn != null) {
                ApiParamDoc apiParamDoc = new ApiParamDoc(modelAttributeAnn.value(), "", paramType, "false", new String[] {},
                        null, null);
                mergeApiQueryParamDoc(apiQueryParam, apiParamDoc);
                apiParamDocs.add(apiParamDoc);
            }
        }

        return apiParamDocs;
    }

    private static Set<ApiParamDoc> getParamDocsFromAnnotation(RequestMapping requestMapping) {
        return Arrays.stream(requestMapping.params())
                     .map(SpringQueryParamBuilder::createParamFromSpringDefinition)
                     .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static ApiParamDoc createParamFromSpringDefinition(String param) {
        String[] splitParam = param.split("=");
        if (splitParam.length <= 1) {
            return createStringParamDoc(param);
        }
        final String name = splitParam[0];
        final String value = splitParam[1];
        return createStringParamDoc(name, value);
    }

    private static ApiParamDoc createStringParamDoc(String name, String... values) {
        LivedocType stringType = LivedocTypeBuilder.build(String.class);
        return new ApiParamDoc(name, "", stringType, "true", values, null, null);
    }

    private static ApiParamDoc getParamDocFromRequestParam(Method method, int i, RequestParam requestParamAnn,
            LivedocType paramType) {
        String name = getSpringParamName(method, requestParamAnn, i);
        String required = String.valueOf(requestParamAnn.required());
        String defaultvalue = requestParamAnn.defaultValue().equals(ValueConstants.DEFAULT_NONE) ?
                "" :
                requestParamAnn.defaultValue();
        return new ApiParamDoc(name, "", paramType, required, new String[] {}, null,
                defaultvalue);
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
     * Available properties that can be overridden: name, description, required, allowedvalues, format, defaultvalue.
     * Name is overridden only if it's empty in the apiParamDoc argument. Description, format and allowedvalues are
     * copied in any case Default value and required are not overridden: in any case they are coming from the default
     * values of @RequestParam
     *
     * @param apiQueryParam
     * @param apiParamDoc
     */
    private static void mergeApiQueryParamDoc(ApiQueryParam apiQueryParam, ApiParamDoc apiParamDoc) {
        if (apiQueryParam != null) {
            if (apiParamDoc.getName().trim().isEmpty()) {
                apiParamDoc.setName(apiQueryParam.name());
            }
            apiParamDoc.setDescription(apiQueryParam.description());
            apiParamDoc.setAllowedvalues(apiQueryParam.allowedvalues());
            apiParamDoc.setFormat(apiQueryParam.format());
        }
    }

}
