---
title: Quickstart Spring Boot
layout: default
---

# Quickstart: Spring Boot

`livedoc-springboot` is almost identical to the Spring MVC flavour, but you have even less work to do!

Like `livedoc-springmvc`, it knows Spring annotations and is able to generate a documentation without further 
configuration. Combined with [Javadoc processing](../javadoc-processing), it allows you to generate a complete 
documentation without a single Livedoc annotation.

## Add a dependency on `livedoc-springboot`

{% include dependency.md artifactId="livedoc-springboot" %}

If you want to generate part of your documentation (descriptions) from your Javadoc, you also need to 
[add the Livedoc Javadoc processor](../javadoc-processing).

## Add the required configuration in `application.properties`

If not already present, you may create `application.properties` in `src/main/resources` (at the root, without 
package), and add the following content:

```properties
livedoc.packages=com.mycompany.controller,com.mycompany.model,org.example.external.model
```

That's right, the package list is the only required configuration for livedoc.
If you want a bit more control, you may configure other properties:

```properties
livedoc.name=My API
livedoc.version=1.0
livedoc.packages=com.mycompany.controller,com.mycompany.model,org.example.external.model
livedoc.baseUrl=http://localhost:8080/api
livedoc.playgroundEnabled=true
livedoc.displayMethodAs=URI
```

Here is what each of these properties means:

{% include configuration-variables.md %}

### Using generated build info

If you're using the Gradle Spring Boot plugin, you can make your app's name and version available by adding the 
following lines to your `build.gradle`:

```groovy
springBoot {
    buildInfo()
}
```
If the build info is available, Livedoc will fall back to this when you don't provide the name or version in your 
`application.properties`. You can read more information about this in 
[the official documentation of the plugin](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/#integrating-with-actuator-build-info).

## Enable JSON documentation on your configuration class

```java
@SpringBootApplication
@EnableJSONDoc
public class MyWebapp {
    
    public static void main(String[] args) {
        SpringApplication.run(MyWebapp.class, args);
    }
}
```

This will automatically register a controller that serves the documentation as JSON at the `/jsondoc` endpoint.

## Access the documentation

By navigating to the `/jsondoc` endpoint on your server, you may get the JSON description of your documentation.

More interesting, you may also use the provided UI with [very few steps](../livedoc-ui).

## Note on Livedoc's JSON serialization

Sometimes, you'd like to configure a custom Jackson `ObjectMapper` for your API, which is a legitimate need. 
Such a custom mapper could affect Livedoc's JSON output, which in turn could break the Livedoc UI.

This is why `@EnableJSONDoc` also registers a `LivedocMessageConverter`, which handles Livedoc's custom media type 
`application/livedoc+json` returned by `/jsondoc`, using an independent `ObjectMapper`.

This won't affect your own API, but it may be surprising that the JSON produced by `/jsondoc` does not follow your 
configuration, hence this short note.
