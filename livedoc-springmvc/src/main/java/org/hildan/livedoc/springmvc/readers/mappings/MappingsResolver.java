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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class MappingsResolver {

    public static List<String> getRequestMappings(Method method, Class<?> controller) {
        return getMappingsFromGroup(method, controller, requestPathExtractorGroup());
    }

    public static List<String> getMessageMappings(Method method, Class<?> controller) {
        return getMappingsFromGroup(method, controller, messagePathExtractorGroup());
    }

    public static List<String> getSubscribeMappings(Method method, Class<?> controller) {
        List<PathExtractor<?>> messagePathExtractors = messagePathExtractorGroup();
        List<PathExtractor<?>> subscribeExtractors = subscribePathExtractorGroup();
        List<PathExtractor<?>> messageAndSubscribeExtractors = new ArrayList<>(messagePathExtractors);
        messageAndSubscribeExtractors.addAll(subscribeExtractors);

        List<String> prefixes = getPrefixMappings(controller, messageAndSubscribeExtractors);
        List<String> suffixes = getAllMappings(method, subscribeExtractors);
        return PathUtils.joinAll(prefixes, suffixes);
    }

    @NotNull
    private static List<PathExtractor<?>> messagePathExtractorGroup() {
        if (ClasspathUtils.isMessageMappingOnClasspath()) {
            return Collections.singletonList(PathExtractor.of(MessageMapping.class, MessageMapping::value));
        }
        return Collections.emptyList();
    }

    @NotNull
    private static List<PathExtractor<?>> subscribePathExtractorGroup() {
        if (ClasspathUtils.isSubscribeMappingOnClasspath()) {
            return Collections.singletonList(PathExtractor.of(SubscribeMapping.class, SubscribeMapping::value));
        }
        return Collections.emptyList();
    }

    @NotNull
    private static List<PathExtractor<?>> requestPathExtractorGroup() {
        List<PathExtractor<?>> extractors = new ArrayList<>(5);
        if (ClasspathUtils.isRequestMappingOnClasspath()) {
            extractors.add(PathExtractor.of(RequestMapping.class, RequestMapping::value, RequestMapping::path));
        }
        if (ClasspathUtils.isGetMappingOnClasspath()) {
            // even if @GetMapping and the likes are just shortcuts for @RequestMapping, the path has to be retrieved
            // from these annotations, because it is specified as an attribute of these ones, not @RequestMapping
            extractors.add(PathExtractor.of(GetMapping.class, GetMapping::value, GetMapping::path));
            extractors.add(PathExtractor.of(PostMapping.class, PostMapping::value, PostMapping::path));
            extractors.add(PathExtractor.of(PatchMapping.class, PatchMapping::value, PatchMapping::path));
            extractors.add(PathExtractor.of(PutMapping.class, PutMapping::value, PutMapping::path));
            extractors.add(PathExtractor.of(DeleteMapping.class, DeleteMapping::value, DeleteMapping::path));
        }
        return extractors;
    }

    private static List<String> getMappingsFromGroup(Method method, Class<?> controller,
            List<PathExtractor<?>> extractors) {
        List<String> prefixes = getPrefixMappings(controller, extractors);
        List<String> suffixes = getAllMappings(method, extractors);
        return PathUtils.joinAll(prefixes, suffixes);
    }

    @NotNull
    private static List<String> getPrefixMappings(Class<?> controller, List<PathExtractor<?>> extractors) {
        List<String> prefixes = getAllMappings(controller, extractors);
        if (prefixes.isEmpty()) {
            prefixes.add("");
        }
        return prefixes;
    }

    private static List<String> getAllMappings(AnnotatedElement element, List<PathExtractor<?>> extractors) {
        return extractors.stream()
                         .map(e -> e.extractPaths(element))
                         .flatMap(Collection::stream)
                         .collect(Collectors.toList());
    }
}
