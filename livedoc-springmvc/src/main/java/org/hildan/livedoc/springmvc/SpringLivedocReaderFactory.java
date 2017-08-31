package org.hildan.livedoc.springmvc;

import java.util.List;

import org.hildan.livedoc.core.AnnotatedTypesFinder;
import org.hildan.livedoc.core.DocReader;
import org.hildan.livedoc.core.LivedocAnnotationDocReader;
import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.LivedocReaderBuilder;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.util.LivedocUtils;
import org.hildan.livedoc.springmvc.scanner.properties.JacksonPropertyScanner;
import org.reflections.Reflections;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SpringLivedocReaderFactory {

    public static LivedocReader getReader(List<String> packages) {
        Reflections reflections = LivedocUtils.newReflections(packages);
        AnnotatedTypesFinder annotatedTypesFinder = LivedocUtils.createAnnotatedTypesFinder(reflections);

        PropertyScanner propertyScanner = createJacksonPropertyScanner();
        DocReader springDocReader = new SpringDocReader(annotatedTypesFinder);
        LivedocAnnotationDocReader baseDocReader = new LivedocAnnotationDocReader(annotatedTypesFinder);

        return new LivedocReaderBuilder().scanningPackages(packages)
                                         .withPropertyScanner(propertyScanner)
                                         .addDocReader(springDocReader)
                                         .addDocReader(baseDocReader)
                                         .build();
    }

    private static PropertyScanner createJacksonPropertyScanner() {
        // to match the spring config without accessing the actual bean containing it
        ObjectMapper jacksonObjectMapper = Jackson2ObjectMapperBuilder.json().build();
        return new JacksonPropertyScanner(jacksonObjectMapper);
    }
}
