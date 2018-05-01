package org.hildan.livedoc.springmvc.readers.request;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.model.doc.ParamDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.readers.javadoc.JavadocHelper;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

class QueryParamReader {

    private static final DefaultParameterNameDiscoverer PARAM_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    static List<ParamDoc> buildParamDocs(Method method, Class<?> controller,
            TypeReferenceProvider typeReferenceProvider) {
        List<ParamDoc> paramDocs = new ArrayList<>();

        RequestMapping requestMapping = controller.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            paramDocs.addAll(getParamDocsFromAnnotation(method, requestMapping, typeReferenceProvider));
        }

        requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            paramDocs.addAll(getParamDocsFromAnnotation(method, requestMapping, typeReferenceProvider));
        }

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            RequestParam requestParamAnn = param.getAnnotation(RequestParam.class);
            ModelAttribute modelAttributeAnn = param.getAnnotation(ModelAttribute.class);
            ApiQueryParam apiQueryParam = param.getAnnotation(ApiQueryParam.class);

            LivedocType paramType = typeReferenceProvider.getReference(param.getParameterizedType());
            if (requestParamAnn != null) {
                ParamDoc paramDoc = getParamDocFromRequestParam(method, i, requestParamAnn, paramType);
                mergeApiQueryParamDoc(apiQueryParam, paramDoc);
                paramDocs.add(paramDoc);
            }

            if (modelAttributeAnn != null) {
                ParamDoc paramDoc = getParamDocFromModelAttribute(method, i, modelAttributeAnn, paramType);
                mergeApiQueryParamDoc(apiQueryParam, paramDoc);
                paramDocs.add(paramDoc);
            }
        }

        return paramDocs;
    }

    private static List<ParamDoc> getParamDocsFromAnnotation(Method method, RequestMapping requestMapping,
            TypeReferenceProvider typeReferenceProvider) {
        return Arrays.stream(requestMapping.params())
                     .map(param -> createParamFromSpringDefinition(method, param, typeReferenceProvider))
                     .collect(Collectors.toList());
    }

    private static ParamDoc createParamFromSpringDefinition(Method method, String paramStr,
            TypeReferenceProvider typeReferenceProvider) {
        String[] splitParam = paramStr.split("=");
        if (splitParam.length <= 1) {
            return createStringParamDoc(typeReferenceProvider, paramStr, method);
        }
        String name = splitParam[0];
        String value = splitParam[1];
        return createStringParamDoc(typeReferenceProvider, name, method, value);
    }

    private static ParamDoc createStringParamDoc(TypeReferenceProvider typeReferenceProvider, String name,
            Method method, String... values) {
        String description = JavadocHelper.getJavadocDescription(method, name).orElse(null);
        LivedocType stringType = typeReferenceProvider.getReference(String.class);
        return new ParamDoc(name, description, stringType, "true", values, null, null);
    }

    private static ParamDoc getParamDocFromRequestParam(Method method, int i, RequestParam requestParamAnn,
            LivedocType paramType) {
        String name = getSpringParamName(method, requestParamAnn, i);
        String description = JavadocHelper.getJavadocDescription(method, name).orElse(null);
        String required = String.valueOf(requestParamAnn.required());
        boolean hasDefault = requestParamAnn.defaultValue().equals(ValueConstants.DEFAULT_NONE);
        String defaultvalue = hasDefault ? null : requestParamAnn.defaultValue();
        return new ParamDoc(name, description, paramType, required, new String[0], null, defaultvalue);
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

    private static ParamDoc getParamDocFromModelAttribute(Method method, int i, ModelAttribute modelAttributeAnn,
            LivedocType paramType) {
        String name = getSpringParamName(method, modelAttributeAnn, i);
        String description = JavadocHelper.getJavadocDescription(method, name).orElse(null);
        return new ParamDoc(name, description, paramType, "false", new String[0], null, null);
    }

    private static String getSpringParamName(Method method, ModelAttribute modelAttribute, int index) {
        if (!modelAttribute.name().isEmpty()) {
            return modelAttribute.name();
        }
        if (!modelAttribute.value().isEmpty()) {
            return modelAttribute.value();
        }
        return PARAM_NAME_DISCOVERER.getParameterNames(method)[index];
    }

    private static void mergeApiQueryParamDoc(ApiQueryParam apiQueryParam, ParamDoc paramDoc) {
        if (apiQueryParam != null) {
            String livedocName = nullifyIfEmpty(apiQueryParam.name());
            if (livedocName != null) {
                paramDoc.setName(livedocName);
            }
            if (paramDoc.getDescription() == null) {
                paramDoc.setDescription(nullifyIfEmpty(apiQueryParam.description()));
            }
            paramDoc.setAllowedValues(apiQueryParam.allowedValues());
            paramDoc.setFormat(apiQueryParam.format());
        }
    }

}
