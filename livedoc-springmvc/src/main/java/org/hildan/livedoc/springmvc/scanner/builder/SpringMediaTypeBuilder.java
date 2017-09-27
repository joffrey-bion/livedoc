package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;

public class SpringMediaTypeBuilder {

    /**
     * Reads the media types produced by the given method. As defined in Spring documentation, method-level values take
     * precedence over type-level values.
     *
     * @param method
     *         the method to read method-level information from
     * @param controller
     *         the type to read type-level information from
     *
     * @return the media types produced by the given method
     */
    public static Set<String> buildProduces(Method method, Class<?> controller) {
        return readMediaType(method, controller, RequestMapping::produces);
    }

    /**
     * Reads the media types consumed by the given method. As defined in Spring documentation, method-level values take
     * precedence over type-level values.
     *
     * @param method
     *         the method to read method-level information from
     * @param controller
     *         the type to read type-level information from
     *
     * @return the media types consumed by the given method
     */
    public static Set<String> buildConsumes(Method method, Class<?> controller) {
        return readMediaType(method, controller, RequestMapping::consumes);
    }

    private static Set<String> readMediaType(Method method, Class<?> controller,
            Function<RequestMapping, String[]> extractor) {

        // by definition, method-level values have precedence
        String[] methodLevelValues = extractFrom(method, extractor);
        if (methodLevelValues.length > 0) {
            return new LinkedHashSet<>(Arrays.asList(methodLevelValues));
        }

        String[] typeLevelValues = extractFrom(controller, extractor);
        if (typeLevelValues.length > 0) {
            return new LinkedHashSet<>(Arrays.asList(typeLevelValues));
        }

        return Collections.singleton(MediaType.APPLICATION_JSON_VALUE);
    }

    private static String[] extractFrom(AnnotatedElement element, Function<RequestMapping, String[]> extractor) {
        RequestMapping requestMapping = element.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            return extractor.apply(requestMapping);
        }
        return new String[0];
    }
}
