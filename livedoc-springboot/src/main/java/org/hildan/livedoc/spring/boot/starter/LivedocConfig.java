package org.hildan.livedoc.spring.boot.starter;

import org.hildan.livedoc.springmvc.controller.JsonLivedocController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LivedocProperties.class)
@ConditionalOnClass(JsonLivedocController.class)
public class LivedocConfig {

    final static String LIVEDOC_PROPERTIES_PREFIX = "livedoc";

    @Autowired
    private LivedocProperties properties;

    @Bean
    public JsonLivedocController jsonLivedocController() {
        JsonLivedocController controller = new JsonLivedocController(properties.getVersion(), properties.getPackages());
        controller.setBasePath(properties.getBasePath());
        controller.setPlaygroundEnabled(properties.isPlaygroundEnabled());
        controller.setDisplayMethodAs(properties.getDisplayMethodAs());
        return controller;
    }

}
