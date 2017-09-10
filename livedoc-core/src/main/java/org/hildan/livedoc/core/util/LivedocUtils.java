package org.hildan.livedoc.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hildan.livedoc.core.AnnotatedTypesFinder;
import org.hildan.livedoc.core.LivedocReader;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LivedocUtils {

    private static final Logger logger = LoggerFactory.getLogger(LivedocReader.class);

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
}
