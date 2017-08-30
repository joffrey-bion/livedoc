package org.hildan.livedoc.springmvc;

import java.util.List;

import org.hildan.livedoc.core.LivedocBuilder;
import org.hildan.livedoc.core.scanner.AnnotatedTypesFinder;
import org.hildan.livedoc.core.scanner.DocReader;
import org.hildan.livedoc.core.scanner.LivedocAnnotationDocReader;
import org.hildan.livedoc.core.scanner.properties.PropertyScanner;
import org.hildan.livedoc.core.scanner.types.RecursivePropertyTypesScanner;
import org.hildan.livedoc.core.scanner.types.mappers.ConcreteTypesMapper;
import org.hildan.livedoc.core.util.LivedocUtils;
import org.hildan.livedoc.springmvc.scanner.SpringDocReader;
import org.hildan.livedoc.springmvc.scanner.properties.JacksonPropertyScanner;
import org.reflections.Reflections;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SpringLivedocBuilderFactory {

    public static LivedocBuilder springLivedocBuilder(List<String> packages) {
        Reflections reflections = LivedocUtils.newReflections(packages);
        AnnotatedTypesFinder annotatedTypesFinder = LivedocUtils.createAnnotatedTypesFinder(reflections);

        PropertyScanner propertyScanner = createJacksonPropertyScanner();
        DocReader springDocReader = new SpringDocReader(annotatedTypesFinder);
        LivedocAnnotationDocReader baseDocReader = new LivedocAnnotationDocReader(annotatedTypesFinder);
        RecursivePropertyTypesScanner typesScanner = new RecursivePropertyTypesScanner(propertyScanner);
        typesScanner.setMapper(new ConcreteTypesMapper(reflections));

        LivedocBuilder builder = new LivedocBuilder(propertyScanner, baseDocReader, springDocReader, baseDocReader);
        builder.setTypesScanner(typesScanner);
        return builder;
    }

    private static PropertyScanner createJacksonPropertyScanner() {
        // to match the spring config without accessing the actual bean containing it
        ObjectMapper jacksonObjectMapper = Jackson2ObjectMapperBuilder.json().build();
        return new JacksonPropertyScanner(jacksonObjectMapper);
    }
}
