package org.hildan.livedoc.core.annotations.global;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hildan.livedoc.core.model.LivedocDefaultType;

import static org.hildan.livedoc.core.model.LivedocDefaultType.DEFAULT_NONE;

/**
 * Describes one page of the global free-text documentation. This annotation can be repeated to declare multiple pages.
 * There is no need to wrap it in the {@link ApiGlobalPages} annotation.
 * <p>
 * This annotation provides multiple fields to specify where to get the content of the page. Exactly one such field
 * should be used in addition to {@link #title()}.
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
     * A string containing the content of the page.
     */
    String content() default DEFAULT_NONE;

    /**
     * A resource path to a file which content should be used as-is for this page. Both absolute and relative resource
     * paths are supported. When a relative path is used, it is resolved relatively to the package of the class
     * annotated with this annotation, as in standard resource loading.
     */
    String resource() default DEFAULT_NONE;

    /**
     * A relative resource path to a FreeMarker template file. The path is relative to the package of the class
     * annotated with this annotation, as in standard resource loading. Absolute paths are not supported by FreeMarker.
     */
    String template() default DEFAULT_NONE;

    /**
     * A {@link PageGenerator} to use to generate the content of this page. The given class will be instantiated at
     * runtime and needs to have a default no-arg constructor.
     */
    Class<? extends PageGenerator> generator() default LivedocDefaultType.class;
}
