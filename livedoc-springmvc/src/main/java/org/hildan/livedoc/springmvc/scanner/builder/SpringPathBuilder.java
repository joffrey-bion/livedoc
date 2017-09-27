package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.hildan.livedoc.springmvc.scanner.utils.ClasspathUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class SpringPathBuilder {

    public static Set<String> buildPath(Method method, Class<?> controller) {
        return getMappings(method, controller, getPathExtractors());
    }

    private static List<PathExtractor<?>> getPathExtractors() {
        List<PathExtractor<?>> extractors = new ArrayList<>(3);
        if (ClasspathUtils.isMessageMappingOnClasspath()) {
            extractors.add(new PathExtractor<>(MessageMapping.class, MessageMapping::value));
        }
        if (ClasspathUtils.isSubscribeMappingOnClasspath()) {
            extractors.add(new PathExtractor<>(SubscribeMapping.class, SubscribeMapping::value));
        }
        if (ClasspathUtils.isRequestMappingOnClasspath()) {
            extractors.add(new PathExtractor<>(RequestMapping.class, RequestMapping::value, RequestMapping::path));
        }
        return extractors;
    }

    private static Set<String> getMappings(Method method, Class<?> controller, List<PathExtractor<?>> extractors) {
        Set<String> paths = new HashSet<>();
        for (PathExtractor<?> extractor : extractors) {
            if (method.isAnnotationPresent(extractor.getAnnotationClass())) {
                paths.addAll(getMappings(method, controller, extractor));
            }
        }
        return paths;
    }

    private static Set<String> getMappings(Method method, Class<?> controller, PathExtractor<?> extractor) {
        Set<String> controllerMappings = extractor.extractPaths(controller);
        Set<String> methodMappings = extractor.extractPaths(method);
        return joinAll(controllerMappings, methodMappings);
    }

    private static Set<String> joinAll(Set<String> pathPrefixes, Set<String> pathsSuffixes) {
        Set<String> mappings = new HashSet<>();
        for (String controllerPath : pathPrefixes) {
            for (String methodPath : pathsSuffixes) {
                mappings.add(join(controllerPath, methodPath));
            }
        }
        return mappings;
    }

    private static String join(String path1, String path2) {
        boolean path1HasSep = path1.endsWith("/");
        boolean path2HasSep = path2.startsWith("/");
        if (path1HasSep && path2HasSep) {
            return path1 + path2.substring(1);
        }
        if (!path1HasSep && !path2HasSep && (path1.isEmpty() || !path2.isEmpty())) {
            return path1 + '/' + path2;
        }
        return path1 + path2;
    }

    private static class PathExtractor<A extends Annotation> {

        private final Class<A> annotationClass;

        private final Function<A, String[]>[] pathsExtractors;

        @SafeVarargs
        private PathExtractor(Class<A> annotationClass, Function<A, String[]>... pathsExtractors) {
            this.annotationClass = annotationClass;
            this.pathsExtractors = pathsExtractors;
        }

        Class<A> getAnnotationClass() {
            return annotationClass;
        }

        Set<String> extractPaths(AnnotatedElement element) {
            A annotation = element.getAnnotation(annotationClass);
            if (annotation == null) {
                return Collections.singleton("");
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
}
