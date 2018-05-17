package org.hildan.livedoc.core.readers;

import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.global.GlobalDoc;
import org.hildan.livedoc.core.templating.GlobalTemplateData;
import org.jetbrains.annotations.NotNull;

/**
 * A component able to build the global documentation of an API (free text pages).
 */
public interface GlobalDocReader {

    @NotNull
    GlobalDoc getApiGlobalDoc(LivedocConfiguration configuration, GlobalTemplateData globalTemplateData);
}
