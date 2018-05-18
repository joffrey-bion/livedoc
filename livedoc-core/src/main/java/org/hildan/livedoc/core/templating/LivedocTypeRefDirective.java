package org.hildan.livedoc.core.templating;

import java.util.Map;

import freemarker.template.TemplateModelException;

public class LivedocTypeRefDirective extends LivedocLinkDirective {

    private static final String PARAM_TYPE = "type";

    @Override
    protected String getReferenceUrl(Map params) throws TemplateModelException {
        String typeRef = DirectiveUtils.getMandatoryString(params, PARAM_TYPE);
        return "type/" + typeRef;
    }
}
