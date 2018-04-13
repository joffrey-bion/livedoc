package org.hildan.livedoc.core.templating;

import java.io.IOException;
import java.io.StringWriter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerUtils {

    public static Configuration createDefaultFreeMarkerConfig() {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(FreeMarkerUtils.class, "");
        return configuration;
    }

    public static String loadTemplateAsString(Configuration configuration, GlobalTemplateData templateData,
            String templateName) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templateName);
        StringWriter out = new StringWriter();
        template.process(templateData, out);
        return out.toString();
    }
}
