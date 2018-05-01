package org.hildan.livedoc.springmvc.readers.mappings;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

class PathExtractor<A extends Annotation> {

    private final Class<A> annotationClass;

    private final List<Function<A, String[]>> pathsExtractors;

    private PathExtractor(Class<A> annotationClass, List<Function<A, String[]>> pathsExtractors) {
        this.annotationClass = annotationClass;
        this.pathsExtractors = pathsExtractors;
    }

    @SafeVarargs
    static <A extends Annotation> PathExtractor<A> of(Class<A> annotationClass,
            Function<A, String[]>... pathsExtractors) {
        return new PathExtractor<>(annotationClass, Arrays.asList(pathsExtractors));
    }

    List<String> extractPaths(AnnotatedElement element) {
        A annotation = element.getAnnotation(annotationClass);
        if (annotation == null) {
            return Collections.emptyList();
        }
        List<String> paths = new ArrayList<>();
        for (Function<A, String[]> pathExtractor : pathsExtractors) {
            Collections.addAll(paths, pathExtractor.apply(annotation));
        }
        if (paths.isEmpty()) {
            paths.add("");
        }
        return paths;
    }
}
