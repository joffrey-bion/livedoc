package org.hildan.livedoc.spring.boot.starter;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hildan.livedoc.springmvc.controller.JsonLivedocController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@EnableConfigurationProperties(LivedocProperties.class)
@ConditionalOnClass(JsonLivedocController.class)
public class LivedocConfig {

    public static final String LIVEDOC_PROPERTIES_PREFIX = "livedoc";

    @Autowired
    private LivedocProperties properties;

    // We don't directly get the ObjectMapper here because reasons:
    // https://stackoverflow.com/questions/30060006/how-do-i-obtain-the-jackson-objectmapper-in-use-by-spring-4-1
    @Autowired(required = false)
    private MappingJackson2HttpMessageConverter springMvcJacksonConverter;

    @Bean
    public JsonLivedocController jsonLivedocController() {
        String version = properties.getVersion();
        List<String> packages = properties.getPackages();
        ObjectMapper objectMapper = getObjectMapper();

        JsonLivedocController controller = new JsonLivedocController(version, packages, objectMapper);
        controller.setBasePath(properties.getBasePath());
        controller.setPlaygroundEnabled(properties.isPlaygroundEnabled());
        controller.setDisplayMethodAs(properties.getDisplayMethodAs());
        return controller;
    }

    private ObjectMapper getObjectMapper() {
        if (springMvcJacksonConverter == null) {
            return null;
        }
        return springMvcJacksonConverter.getObjectMapper();
    }
}
