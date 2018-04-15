package org.hildan.livedoc.core.annotations.global;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes one page of the global free-text documentation. This annotation can be repeated to declare multiple
 * pages. There is no need to wrap it in the {@link ApiGlobalPages} ann
 */
@Documented
@Repeatable(ApiGlobalPages.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
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
