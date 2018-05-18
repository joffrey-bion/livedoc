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
        addDefaultConfiguration(configuration);
        return configuration;
    }

    public static void addDefaultConfiguration(Configuration configuration) {
        configuration.setSharedVariable("type_ref", new LivedocTypeRefDirective());
        configuration.setSharedVariable("api_ref", new LivedocApiRefDirective());
    }

    public static String loadTemplateAsString(Configuration configuration, GlobalTemplateData templateData,
            String templateName) throws IOException, TemplateException {
        if (templateName.startsWith("/")) {
            throw new IllegalArgumentException("Absolute template paths are not supported by FreeMarker, got '%s'. "
                    + "Please use a path that's relative to the class using it.");
        }
        Template template = configuration.getTemplate(templateName);
        StringWriter out = new StringWriter();
        template.process(templateData, out);
        return out.toString();
    }

}
