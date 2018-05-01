package org.hildan.livedoc.springmvc.readers.mappings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.model.doc.ParamDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.readers.javadoc.JavadocHelper;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.web.bind.annotation.PathVariable;

public class PathVariableReader<T extends Annotation> {

    private static final DefaultParameterNameDiscoverer PARAM_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    private final Class<T> pathVariableAnnotationType;

    private final Function<T, String> paramNameExtractor;

    private PathVariableReader(Class<T> pathVariableAnnotationType, Function<T, String> paramNameExtractor) {
        this.pathVariableAnnotationType = pathVariableAnnotationType;
        this.paramNameExtractor = paramNameExtractor;
    }

    public static List<ParamDoc> buildPathVariableDocs(Method method, TypeReferenceProvider typeRefProvider) {
        Function<PathVariable, String> extractor = ann -> ann.name().isEmpty() ? ann.value() : ann.name();
        PathVariableReader<PathVariable> reader = new PathVariableReader<>(PathVariable.class, extractor);
        return reader.buildParamDocs(method, typeRefProvider);
    }

    public static List<ParamDoc> buildDestinationVariableDocs(Method method, TypeReferenceProvider typeRefProvider) {
        Function<DestinationVariable, String> extractor = DestinationVariable::value;
        PathVariableReader<DestinationVariable> reader = new PathVariableReader<>(DestinationVariable.class, extractor);
        return reader.buildParamDocs(method, typeRefProvider);
    }

    private List<ParamDoc> buildParamDocs(Method method, TypeReferenceProvider typeRefProvider) {
        List<ParamDoc> paramDocs = new ArrayList<>();

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            T springAnn = param.getAnnotation(pathVariableAnnotationType);
            if (springAnn != null) {
                ParamDoc paramDoc = buildParamDoc(method, typeRefProvider, i, param, springAnn);
                paramDocs.add(paramDoc);
            }
        }

        return paramDocs;
    }

    @NotNull
    private ParamDoc buildParamDoc(Method method, TypeReferenceProvider typeReferenceProvider, int i, Parameter param,
            T pathVariable) {
        LivedocType livedocType = typeReferenceProvider.getReference(param.getParameterizedType());
        String paramName = getSpringParamName(method, pathVariable, i);
        String description = JavadocHelper.getJavadocDescription(method, paramName).orElse(null);
        ParamDoc paramDoc = new ParamDoc(paramName, description, livedocType, "true", new String[0], null, null);

        ApiPathParam pathParamAnn = param.getAnnotation(ApiPathParam.class);
        if (pathParamAnn != null) {
            mergeApiPathParamDoc(pathParamAnn, paramDoc);
        }
        return paramDoc;
    }

    private String getSpringParamName(Method method, T pathVariableAnnotation, int paramIndex) {
        String paramName = paramNameExtractor.apply(pathVariableAnnotation);
        if (!paramName.isEmpty()) {
            return paramName;
        }
        return PARAM_NAME_DISCOVERER.getParameterNames(method)[paramIndex];
    }

    private static void mergeApiPathParamDoc(ApiPathParam apiPathParam, ParamDoc paramDoc) {
        if (!apiPathParam.name().trim().isEmpty()) {
            paramDoc.setName(apiPathParam.name());
        }
        if (!apiPathParam.description().trim().isEmpty()) {
            paramDoc.setDescription(apiPathParam.description());
        }
        if (apiPathParam.allowedValues().length > 0) {
            paramDoc.setAllowedValues(apiPathParam.allowedValues());
        }
        if (!apiPathParam.format().trim().isEmpty()) {
            paramDoc.setFormat(apiPathParam.format());
        }
    }
}
