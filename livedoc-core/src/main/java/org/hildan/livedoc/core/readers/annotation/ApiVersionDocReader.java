package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.AnnotatedElement;

import org.hildan.livedoc.core.annotations.ApiVersion;
import org.hildan.livedoc.core.model.doc.version.ApiVersionDoc;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

public class ApiVersionDocReader {

    public static ApiVersionDoc read(Class<?> clazz) {
        return read(clazz, null);
    }

    /**
     * Reads an {@link ApiVersionDoc} from the given annotated element. In case this annotation is present at type and
     * method level, then the method annotation will override the type one.
     *
     * @param element
     *         the element to read the version for
     * @param defaultDoc
     *         the default doc to return if none could be read on the given element
     *
     * @return a new {@link ApiVersionDoc} for given element, or the given defaultDoc if none could be read
     */
    public static ApiVersionDoc read(AnnotatedElement element, ApiVersionDoc defaultDoc) {
        ApiVersion elementAnnotation = element.getAnnotation(ApiVersion.class);
        if (elementAnnotation != null) {
            return buildFromAnnotation(element.getAnnotation(ApiVersion.class));
        }
        return defaultDoc;
    }

    private static ApiVersionDoc buildFromAnnotation(ApiVersion annotation) {
        ApiVersionDoc apiVersionDoc = new ApiVersionDoc();
        apiVersionDoc.setSince(annotation.since());
        apiVersionDoc.setUntil(nullifyIfEmpty(annotation.until()));
        return apiVersionDoc;
    }
}
