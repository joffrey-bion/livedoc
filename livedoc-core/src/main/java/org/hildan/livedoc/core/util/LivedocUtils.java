package org.hildan.livedoc.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hildan.livedoc.core.AnnotatedTypesFinder;
import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.pojo.Groupable;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LivedocUtils {

    private static final Logger logger = LoggerFactory.getLogger(LivedocReader.class);

    public static Integer getIndexOfParameterWithAnnotation(Method method, Class<?> a) {
        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parametersAnnotations.length; i++) {
            for (int j = 0; j < parametersAnnotations[i].length; j++) {
                if (a.equals(parametersAnnotations[i][j].annotationType())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static <T extends Groupable> Map<String, Set<T>> group(Iterable<T> elements) {
        Map<String, Set<T>> groupedElements = new TreeMap<>();
        for (T e : elements) {
            String groupName = e.getGroup();
            groupedElements.putIfAbsent(groupName, new TreeSet<>());
            Set<T> group = groupedElements.get(groupName);
            group.add(e);
        }
        return groupedElements;
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

    public static AnnotatedTypesFinder createAnnotatedTypesFinder(List<String> packages) {
        return createAnnotatedTypesFinder(newReflections(packages));
    }

    public static AnnotatedTypesFinder createAnnotatedTypesFinder(Reflections reflections) {
        return annotationClass -> reflections.getTypesAnnotatedWith(annotationClass, true);
    }
}
