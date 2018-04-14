package org.hildan.livedoc.core.annotations.global;

/**
 * Describes one page of the global free-text documentation.
 */
public @interface ApiGlobalPage {

    /**
     * The title of the page, to refer to the page from the list of pages. It is also used to create the page's ID, so
     * it must be unique for each page.
     */
    String title();

    /**
     * The content of the page, which can be provided in different forms depending on the selected {@link #type()}. See
     * {@link PageContentType} for more details.
     */
    String content();

    /**
     * The type of content provided as {@link #content()}. See {@link PageContentType} for more details.
     */
    PageContentType type() default PageContentType.STRING;
}
