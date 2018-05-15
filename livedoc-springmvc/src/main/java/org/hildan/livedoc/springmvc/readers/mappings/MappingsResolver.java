package org.hildan.livedoc.springmvc.readers.mappings;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hildan.livedoc.springmvc.utils.ClasspathUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class MappingsResolver {

    public static List<String> getRequestMappings(Method method, Class<?> controller) {
        return getJoinedMappings(method, controller, PathExtractor.of(RequestMapping.class, RequestMapping::path));
    }

    public static List<String> getMessageMappings(Method method, Class<?> controller) {
        if (!ClasspathUtils.isMessageMappingOnClasspath()) {
            return Collections.emptyList();
        }
        return getJoinedMappings(method, controller, PathExtractor.of(MessageMapping.class, MessageMapping::value));
    }

    public static List<String> getSubscribeMappings(Method method, Class<?> controller) {
        if (!ClasspathUtils.isSubscribeMappingOnClasspath()) {
            return Collections.emptyList();
        }
        List<PathExtractor<?>> messagePathExtractors = messagePathExtractorGroup();
        List<PathExtractor<?>> subscribeExtractors = subscribePathExtractorGroup();
        List<PathExtractor<?>> messageAndSubscribeExtractors = new ArrayList<>(messagePathExtractors);
        messageAndSubscribeExtractors.addAll(subscribeExtractors);

        List<String> prefixes = getPrefixMappings(controller, messageAndSubscribeExtractors);
        List<String> suffixes = getElementMappings(method, subscribeExtractors);
        return PathUtils.joinAll(prefixes, suffixes);
    }

    @NotNull
    private static List<PathExtractor<?>> messagePathExtractorGroup() {
        return Collections.singletonList(PathExtractor.of(MessageMapping.class, MessageMapping::value));
    }

    @NotNull
    private static List<PathExtractor<?>> subscribePathExtractorGroup() {
        return Collections.singletonList(PathExtractor.of(SubscribeMapping.class, SubscribeMapping::value));
    }

    @NotNull
    private static List<String> getJoinedMappings(Method method, Class<?> controller, PathExtractor<?> extractor) {
        List<String> prefixes = getPrefixMappings(controller, Collections.singletonList(extractor));
        List<String> suffixes = getElementMappings(method, Collections.singletonList(extractor));
        return PathUtils.joinAll(prefixes, suffixes);
    }

    @NotNull
    private static List<String> getPrefixMappings(Class<?> controller, List<PathExtractor<?>> extractors) {
        List<String> prefixes = getElementMappings(controller, extractors);
        if (prefixes.isEmpty()) {
            prefixes.add("");
        }
        return prefixes;
    }

    @NotNull
    private static List<String> getElementMappings(AnnotatedElement element, List<PathExtractor<?>> extractors) {
        return extractors.stream()
                         .map(e -> e.extractPaths(element))
                         .flatMap(Collection::stream)
                         .collect(Collectors.toList());
    }
}
