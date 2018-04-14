package org.hildan.livedoc.core.annotations.global;

public enum PageContentType {
    /**
     * The content is provided as a raw string, to use as-is.
     */
    STRING,
    /**
     * The content is an absolute resource path to a file which content should be used as-is. Relative resource paths
     * are not yet supported.
     */
    TEXT_FILE,
    /**
     * The content is a relative resource path to a FreeMarker template file. The path is relative to the package
     * of the class annotated with {@link ApiGlobal}, as in standard resource loading. Absolute paths are not
     * supported by FreeMarker.
     */
    FREEMARKER
}
