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
# mandatory configuration
livedoc.version=1.0
livedoc.packages[0]=com.mycompany.controller #packages in which you have your spring controllers
livedoc.packages[1]=com.mycompany.model #packages in which you have your model classes
livedoc.packages[2]=org.example.external.model #they can also belong to external jars
# optional configuration
livedoc.baseUrl=http://localhost:8080/api #defaults to current server and context path
livedoc.playgroundEnabled=true
livedoc.displayMethodAs=URI
```

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

