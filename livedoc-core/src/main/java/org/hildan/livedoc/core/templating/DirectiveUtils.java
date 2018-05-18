package org.hildan.livedoc.core.templating;

import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

class DirectiveUtils {

    private static final String MISSING_PARAM_MSG = "Missing required param '%s'";

    private static final String NOT_A_STRING = "Param '%s' must be a string, but was of type %s";

    @NotNull
    static String getMandatoryString(Map params, String paramName) throws TemplateModelException {
        return getOptionalString(params, paramName).orElseThrow(() -> missingParamException(paramName));
    }

    @NotNull
    private static TemplateModelException missingParamException(String paramName) {
        return new TemplateModelException(String.format(MISSING_PARAM_MSG, paramName));
    }

    @NotNull
    static Optional<String> getOptionalString(Map params, String paramName) throws TemplateModelException {
        Object value = params.get(paramName);
        if (value == null) {
            return Optional.empty();
        }
        if (!(value instanceof TemplateScalarModel)) {
            throw new TemplateModelException(String.format(NOT_A_STRING, paramName, value.getClass()));
        }
        TemplateScalarModel scalar = (TemplateScalarModel) value;
        return Optional.of(scalar.getAsString());
    }
}
