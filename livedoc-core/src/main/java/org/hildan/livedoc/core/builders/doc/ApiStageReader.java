package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.AnnotatedElement;

import org.hildan.livedoc.core.annotations.ApiStage;
import org.hildan.livedoc.core.model.doc.Stage;

public class ApiStageReader {

    public static Stage read(AnnotatedElement element) {
        return read(element, null);
    }

    public static Stage read(AnnotatedElement element, Stage fallback) {
        ApiStage stage = element.getAnnotation(ApiStage.class);
        return stage != null ? stage.value() : fallback;
    }
}
