package org.hildan.livedoc.core.templating;

import java.util.Map;

import freemarker.template.TemplateModelException;

public class LivedocApiRefDirective extends LivedocLinkDirective {

    private static final String PARAM_API = "api";

    private static final String PARAM_OPERATION = "operation";

    @Override
    protected String getReferenceUrl(Map params) throws TemplateModelException {
        String apiRef = DirectiveUtils.getMandatoryString(params, PARAM_API);
        String opRef = DirectiveUtils.getOptionalString(params, PARAM_OPERATION).map(op -> "/" + op).orElse("");

        return "api/" + apiRef + opRef;
    }
}
