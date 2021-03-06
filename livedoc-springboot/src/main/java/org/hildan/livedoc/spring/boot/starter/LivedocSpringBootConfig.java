package org.hildan.livedoc.spring.boot.starter;

import java.util.List;

import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.springmvc.controller.JsonLivedocController;
import org.hildan.livedoc.springmvc.converter.LivedocMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableConfigurationProperties(LivedocProperties.class)
@PropertySource(value = "classpath:META-INF/build-info.properties", ignoreResourceNotFound = true)
@ConditionalOnClass(JsonLivedocController.class)
public class LivedocSpringBootConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private LivedocProperties properties;

    // We don't directly get the ObjectMapper here because reasons:
    // https://stackoverflow.com/questions/30060006/how-do-i-obtain-the-jackson-objectmapper-in-use-by-spring-4-1
    @Autowired(required = false)
    private MappingJackson2HttpMessageConverter springMvcJacksonConverter;

    @Bean
    public JsonLivedocController jsonLivedocController() {
        ApiMetaData apiInfo = getApiMetaData(properties);
        LivedocConfiguration config = getLivedocConfiguration(properties);
        ObjectMapper objectMapper = getObjectMapper();
        return new JsonLivedocController(apiInfo, config, objectMapper);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // this is to make sure the ObjectMapper used to send the JSON livedoc is independent from the user
        converters.add(0, new LivedocMessageConverter());
    }

    private static LivedocConfiguration getLivedocConfiguration(LivedocProperties properties) {
        List<String> packages = properties.getPackages();
        if (packages == null) {
            throw new IllegalStateException("No package white list provided. Please add some packages to your "
                    + "application.properties (refer to Livedoc Spring Boot documentation)");
        }
        LivedocConfiguration config = new LivedocConfiguration(packages);
        config.setPlaygroundEnabled(properties.isPlaygroundEnabled());
        config.setDisplayMethodAs(properties.getDisplayMethodAs());
        return config;
    }

    private ApiMetaData getApiMetaData(LivedocProperties properties) {
        ApiMetaData apiInfo = new ApiMetaData();
        apiInfo.setName(properties.getName().orElseGet(() -> env.getProperty("build.name")));
        apiInfo.setVersion(properties.getVersion().orElseGet(() -> env.getProperty("build.version")));
        apiInfo.setBaseUrl(properties.getBaseUrl());
        return apiInfo;
    }

    private ObjectMapper getObjectMapper() {
        if (springMvcJacksonConverter == null) {
            return null;
        }
        return springMvcJacksonConverter.getObjectMapper();
    }
}
