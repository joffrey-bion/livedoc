package org.hildan.livedoc.springmvc.readers.mappings;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.springframework.core.annotation.AnnotatedElementUtils;

class PathExtractor<A extends Annotation> {

    private final Class<A> annotationClass;

    private final Function<A, String[]> pathsExtractor;

    private PathExtractor(Class<A> annotationClass, Function<A, String[]> pathsExtractor) {
        this.annotationClass = annotationClass;
        this.pathsExtractor = pathsExtractor;
    }

    static <A extends Annotation> PathExtractor<A> of(Class<A> annotationClass,
            Function<A, String[]> pathsExtractor) {
        return new PathExtractor<>(annotationClass, pathsExtractor);
    }

    List<String> extractPaths(AnnotatedElement element) {
        A annotation = AnnotatedElementUtils.findMergedAnnotation(element, annotationClass);
        if (annotation == null) {
            return Collections.emptyList();
        }
        List<String> paths = new ArrayList<>();
        Collections.addAll(paths, pathsExtractor.apply(annotation));
        if (paths.isEmpty()) {
            paths.add("");
        }
        return paths;
    }
}
