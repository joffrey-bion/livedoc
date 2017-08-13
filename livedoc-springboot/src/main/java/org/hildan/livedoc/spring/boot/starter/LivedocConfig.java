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
    public JsonLivedocController jController() {
        JsonLivedocController controller = new JsonLivedocController(this.properties.getVersion(),
                this.properties.getBasePath(), this.properties.getPackages());
        controller.setPlaygroundEnabled(this.properties.isPlaygroundEnabled());
        controller.setDisplayMethodAs(this.properties.getDisplayMethodAs());
        return controller;
    }

}
