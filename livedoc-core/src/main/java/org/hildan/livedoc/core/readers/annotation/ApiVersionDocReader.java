package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.AnnotatedElement;

import org.hildan.livedoc.core.annotations.ApiVersion;
import org.hildan.livedoc.core.model.doc.version.VersionDoc;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

public class ApiVersionDocReader {

    public static VersionDoc read(Class<?> clazz) {
        return read(clazz, null);
    }

    /**
     * Reads an {@link VersionDoc} from the given annotated element. In case this annotation is present at type and
     * method level, then the method annotation will override the type one.
     *
     * @param element
     *         the element to read the version for
     * @param defaultDoc
     *         the default doc to return if none could be read on the given element
     *
     * @return a new {@link VersionDoc} for given element, or the given defaultDoc if none could be read
     */
    public static VersionDoc read(AnnotatedElement element, VersionDoc defaultDoc) {
        ApiVersion elementAnnotation = element.getAnnotation(ApiVersion.class);
        if (elementAnnotation != null) {
            return buildFromAnnotation(element.getAnnotation(ApiVersion.class));
        }
        return defaultDoc;
    }

    private static VersionDoc buildFromAnnotation(ApiVersion annotation) {
        VersionDoc versionDoc = new VersionDoc();
        versionDoc.setSince(annotation.since());
        versionDoc.setUntil(nullifyIfEmpty(annotation.until()));
        return versionDoc;
    }
}
