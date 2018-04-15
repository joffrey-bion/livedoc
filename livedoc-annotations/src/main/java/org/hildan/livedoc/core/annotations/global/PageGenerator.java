package org.hildan.livedoc.core.annotations.global;

import org.hildan.livedoc.core.templating.GlobalTemplateData;

/**
 * A simple interface to generate the content of a global documentation page. Implementations should be used as
 * arguments for {@link ApiGlobalPage#generator()}.
 */
public interface PageGenerator {

    /**
     * Generates the content of a global doc page.
     *
     * @param templateData
     *         general meta data about the API or Livedoc, which might be useful to create the page's content
     *
     * @return a String representing the HTML content of the page
     */
    String generate(GlobalTemplateData templateData);
}
