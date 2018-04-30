package org.hildan.livedoc.core.model.doc.global;

import java.util.List;

/**
 * The global documentation of the API, containing free-text pages to help the end user understand the rest of the doc.
 */
public class GlobalDoc {

    private List<GlobalDocPage> pages;

    /**
     * Gets a list of free-text pages to add information about the generated documentation.
     *
     * @return a list of free-text pages to add information about the generated documentation
     */
    public List<GlobalDocPage> getPages() {
        return pages;
    }

    public void setPages(List<GlobalDocPage> pages) {
        this.pages = pages;
    }

    /**
     * Gets the ID of the page to use by default.
     *
     * @return the ID of the page to use by default.
     */
    public String getHomePageId() {
        return pages.isEmpty() ? null : pages.get(0).getLivedocId();
    }
}
