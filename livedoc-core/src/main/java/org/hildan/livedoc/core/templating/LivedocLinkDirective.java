package org.hildan.livedoc.core.templating;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public abstract class LivedocLinkDirective implements TemplateDirectiveModel {

    private static final String HREF_TEMPLATE = "livedoc://%s";

    private static final String ANCHOR_OPEN_TEMPLATE = "<a href=\"%s\">";

    private static final String ANCHOR_CLOSE = "</a>";

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        if (loopVars.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables.");
        }

        String href = String.format(HREF_TEMPLATE, getReferenceUrl(params));
        renderAnchorTag(env, body, href);
    }

    protected abstract String getReferenceUrl(Map params) throws TemplateModelException;

    private static void renderAnchorTag(Environment env, TemplateDirectiveBody body, String href)
            throws IOException, TemplateException {
        String anchorOpen = String.format(ANCHOR_OPEN_TEMPLATE, href);
        env.getOut().write(anchorOpen);
        if (body != null) {
            body.render(env.getOut());
        }
        env.getOut().write(ANCHOR_CLOSE);
    }
}
