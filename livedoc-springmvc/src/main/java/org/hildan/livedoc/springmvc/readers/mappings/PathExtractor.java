package org.hildan.livedoc.springmvc.readers.mappings;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    Set<String> extractPaths(AnnotatedElement element) {
        A annotation = element.getAnnotation(annotationClass);
        if (annotation == null) {
            return Collections.emptySet();
        }
        Set<String> paths = new HashSet<>();
        for (Function<A, String[]> pathExtractor : pathsExtractors) {
            Collections.addAll(paths, pathExtractor.apply(annotation));
        }
        if (paths.isEmpty()) {
            paths.add("");
        }
        return paths;
    }
}
