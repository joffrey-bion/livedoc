package org.hildan.livedoc.core.model;

import org.hildan.livedoc.core.annotations.global.PageGenerator;
import org.hildan.livedoc.core.templating.GlobalTemplateData;

public class LivedocDefaultType implements PageGenerator {

    /**
     * Constant defining a value for no default - as a replacement for {@code null} which we cannot use in annotation
     * attributes.
     * <p>This is an artificial arrangement of 16 unicode characters, with its sole purpose being to never match
     * user-declared values.
     */
    public static final String DEFAULT_NONE = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";

    @Override
    public String generate(GlobalTemplateData templateData) {
        return null;
    }
}
