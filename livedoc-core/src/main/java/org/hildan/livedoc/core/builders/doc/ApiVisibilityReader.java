package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.AnnotatedElement;

import org.hildan.livedoc.core.annotations.ApiVisibility;
import org.hildan.livedoc.core.model.doc.Visibility;

public class ApiVisibilityReader {

    public static Visibility read(AnnotatedElement element) {
        return read(element, null);
    }

    public static Visibility read(AnnotatedElement element, Visibility fallback) {
        ApiVisibility visibility = element.getAnnotation(ApiVisibility.class);
        return visibility != null ? visibility.value() : fallback;
    }
}
