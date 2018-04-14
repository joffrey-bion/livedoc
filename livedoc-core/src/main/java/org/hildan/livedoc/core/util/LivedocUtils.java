package org.hildan.livedoc.core.util;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.scanners.AnnotatedTypesFinder;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LivedocUtils {

    private static final Logger logger = LoggerFactory.getLogger(LivedocReader.class);

    public static String getLivedocId(Class<?> clazz) {
        return clazz.getCanonicalName();
    }

    public static String asLivedocId(String userText) {
        try {
            return URLEncoder.encode(userText.toLowerCase(), StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // The system should always have UTF_8
            throw new RuntimeException("Careless code change led here", e);
        }
    }

    public static int getIndexOfParameterWithAnnotation(Method method, Class<? extends Annotation> annotationClass) {
        Annotation[][] paramsAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < paramsAnnotations.length; i++) {
            for (Annotation ann : paramsAnnotations[i]) {
                if (annotationClass.equals(ann.annotationType())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static Reflections newReflections(List<String> packages) {
        Set<URL> urls = new HashSet<>();
        FilterBuilder filter = new FilterBuilder();

        logger.debug("Found " + packages.size() + " package(s) to scan...");
        for (String pkg : packages) {
            logger.debug("Adding package to Livedoc recursive scan: " + pkg);
            urls.addAll(ClasspathHelper.forPackage(pkg));
            filter.includePackage(pkg);
        }

        return new Reflections(new ConfigurationBuilder().filterInputsBy(filter)
                                                         .setUrls(urls)
                                                         .addScanners(new MethodAnnotationsScanner()));
    }

    public static AnnotatedTypesFinder createAnnotatedTypesFinder(Reflections reflections) {
        return annotationClass -> reflections.getTypesAnnotatedWith(annotationClass, true);
    }

    public static <T, U extends Comparable<? super U>> Comparator<T> comparingFirstItem(
            Function<? super T, List<? extends U>> keyExtractor) {
        return (a, b) -> {
            List<? extends U> list1 = keyExtractor.apply(a);
            List<? extends U> list2 = keyExtractor.apply(b);
            if (list1.isEmpty()) {
                if (list2.isEmpty()) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if (list2.isEmpty()) {
                    return 1;
                } else {
                    return list1.get(0).compareTo(list2.get(0));
                }
            }
        };
    }
}
