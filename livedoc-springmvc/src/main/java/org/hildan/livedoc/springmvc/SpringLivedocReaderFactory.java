package org.hildan.livedoc.springmvc;

import java.util.List;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.LivedocReaderBuilder;
import org.hildan.livedoc.core.readers.annotation.LivedocAnnotationDocReader;
import org.hildan.livedoc.core.readers.javadoc.JavadocDocReader;
import org.hildan.livedoc.core.scanners.AnnotatedTypesFinder;
import org.hildan.livedoc.core.scanners.properties.JacksonPropertyScanner;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.util.LivedocUtils;
import org.reflections.Reflections;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A helper that internally uses a {@link LivedocReaderBuilder} to configure a {@link LivedocReader} to inspect a Spring
 * application.
 */
public class SpringLivedocReaderFactory {

    /**
     * Creates a Spring-oriented {@link LivedocReader} using a new Jackson {@link ObjectMapper} with Spring defaults.
     *
     * @param packages
     *         the list of packages to scan
     *
     * @return a new {@link LivedocReader}
     */
    public static LivedocReader getReader(List<String> packages) {
        return getReader(packages, null);
    }

    /**
     * Creates a Spring-oriented {@link LivedocReader} with the given configuration.
     *
     * @param packages
     *         the list of packages to scan
     * @param jacksonObjectMapper
     *         the Jackson {@link ObjectMapper} to use for property exploration and template generation, or null to use
     *         a new mapper with Spring defaults
     *
     * @return a new {@link LivedocReader}
     */
    public static LivedocReader getReader(List<String> packages, ObjectMapper jacksonObjectMapper) {
        Reflections reflections = LivedocUtils.newReflections(packages);
        AnnotatedTypesFinder annotatedTypesFinder = LivedocUtils.createAnnotatedTypesFinder(reflections);

        ObjectMapper mapper = jacksonObjectMapper == null ? createDefaultSpringObjectMapper() : jacksonObjectMapper;
        PropertyScanner propertyScanner = new JacksonPropertyScanner(mapper);

        return new LivedocReaderBuilder().scanningPackages(packages)
                                         .withPropertyScanner(propertyScanner)
                                         .addDocReader(new JavadocDocReader())
                                         .addDocReader(new SpringDocReader(annotatedTypesFinder))
                                         .addDocReader(new LivedocAnnotationDocReader(annotatedTypesFinder))
                                         .build();
    }

    private static ObjectMapper createDefaultSpringObjectMapper() {
        return Jackson2ObjectMapperBuilder.json().build();
    }
}
