---
title: Quickstart Spring MVC
layout: default
---

# Quickstart: Spring MVC

`livedoc-springmvc` knows Spring annotations and is able to generate a documentation without further configuration. 
Combined with [Javadoc processing](../javadoc-processing), it allows you to generate a complete documentation without
 a single Livedoc annotation.

If you're not familiar with [Spring Boot](https://projects.spring.io/spring-boot), I recommend taking a look, as the 
integration with Livedoc is even faster than plain Spring MVC. 

## Add a dependency on `livedoc-springmvc`

{% include dependency.md artifactId="livedoc-springmvc" %}

If you want to generate part of your documentation (descriptions) from your Javadoc, you also need to 
[add the Livedoc Javadoc processor](../javadoc-processing).

## Annotate your code

The beauty of the Spring flavours of Livedoc is that you don't need to annotate everything, because Livedoc already 
knows Spring annotations and can generate a doc from them. 

If you enabled Javadoc processing, descriptions will also be automatically generated.

Using annotations should just be a last resort when the default inferred doc does not match you expectations.

## Serve your documentation

### Use the provided JsonLivedocController

`livedoc-springmvc` provides a `JsonLivedocController` class that exposes the documentation as JSON at the `/jsondoc`
 endpoint. Is is autoconfigured when using the Spring Boot flavour of Livedoc. If you're stuck with Spring MVC, you 
 need to configure it yourself.
 
Here is an example Java configuration in Spring MVC:

```java
@Configuration
public class LivedocControllerConfig {

    // This is to get the actual ObjectMapper used by Spring
    @Autowired(required = false)
    private MappingJackson2HttpMessageConverter springMvcJacksonConverter;

    @Bean
    public JsonLivedocController jsonLivedocController() {
        ApiMetaData apiInfo = new ApiMetaData();
        apiInfo.setName("My API");
        apiInfo.setVersion("1.0");
        apiInfo.setBaseUrl("http://localhost:8080/api"); // optional, defaults to the current server

        // these packages are scanned by Livedoc to find controllers
        // they also act as a whitelist for model classes to document
        // (objects outside these packages won't be documented)
        List<String> packages = new ArrayList<>();
        packages.add("com.mycompany.controllers");
        packages.add("com.mycompany.model");
        packages.add("org.example.external.model");

        LivedocConfiguration config = new LivedocConfiguration(packages);
        config.setPlaygroundEnabled(true); // optional
        config.setDisplayMethodAs(MethodDisplay.URI); // optional

        ObjectMapper objectMapper = getSpringObjectMapper();
        return new JsonLivedocController(apiInfo, config, objectMapper);
    }

    private ObjectMapper getSpringObjectMapper() {
        if (springMvcJacksonConverter == null) {
            return null;
        }
        return springMvcJacksonConverter.getObjectMapper();
    }
}
```

Here is an almost equivalent XML configuration (note that the `ObjectMapper` used here is not the one actually used by 
Spring, but it will be a new `ObjectMapper` with Spring defaults):
```xml
<bean id="documentationController" class="org.hildan.livedoc.springmvc.controller.JsonLivedocController">
    <constructor-arg name="apiInfo">
        <bean class="org.hildan.livedoc.core.model.doc.ApiMetaData">
            <property name="name" value="My API" />
            <property name="version" value="1.0" />
            <property name="baseUrl" value="http://localhost:8080/api" /> <!-- optional -->
        </bean>
    </constructor-arg>
    <constructor-arg name="config">
        <bean class="org.hildan.livedoc.core.config.LivedocConfiguration">
            <constructor-arg name="packages">
                <!-- packages you want Livedoc to scan (controllers, model classes, etc.) -->
                <list>
                    <value>com.mycompany.controllers</value>
                    <value>com.mycompany.model</value>
                    <value>org.example.external.model</value>
                </list>
            </constructor-arg>
            <property name="playgroundEnabled" value="true" /> <!-- optional -->
            <property name="displayMethodAs" value="URI" /> <!-- optional -->
        </bean>
    </constructor-arg>
    <constructor-arg name="jacksonObjectMapper">
        <null /> <!-- uses a default ObjectMapper -->
    </constructor-arg>
</bean>
```

### Make Livedoc's JSON serialization independent

Sometimes, you'd like to configure a custom Jackson `ObjectMapper` for your API, which is a legitimate need. 
That being said, if you have done so, your custom mapper could affect Livedoc's JSON output, which in turn could break 
the Livedoc UI.

To use an independent `ObjectMapper` for Livedoc's JSON serialization and avoid potential issues, simply add the 
provided `LivedocMessageConverter` to your Web MVC configuration: 

```java
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
                                     
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, new LivedocMessageConverter());
    }
}
```

The `LivedocMessageConverter` is configured to only support Livedoc's custom media type `application/livedoc+json`, 
and thus won't affect your own API.

## Access the documentation

By navigating to the `/jsondoc` endpoint on your server, you may get the JSON description of your documentation.

More interesting, you may also use the provided UI with [very few steps](../livedoc-ui).
