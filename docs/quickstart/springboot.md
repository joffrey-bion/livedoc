---
title: Quickstart Spring Boot
layout: default
---

# Quickstart: Spring Boot

`livedoc-springboot` is almost identical to the Spring MVC flavour, but you have even less work to do! 

## Add a dependency on `livedoc-springboot`

### Gradle

```groovy
compile 'org.hildan.livedoc:livedoc-springboot:2.1.1'
```

### Maven

```xml
<dependency>
  <groupId>org.hildan.livedoc</groupId>
  <artifactId>livedoc-springboot</artifactId>
  <version>2.1.1</version>
</dependency>
```

## Add the required configuration in `application.properties`

If not already present, you may create `application.properties` in `src/main/resources` (at the root, without 
package), and add the following content:

```properties
# mandatory configuration
livedoc.version=1.0
livedoc.packages[0]=com.mycompany.controller #packages in which you have your spring controllers
livedoc.packages[1]=com.mycompany.model #packages in which you have your model classes
livedoc.packages[2]=org.example.external.model</value> #they can also belong to external jars
# optional configuration
livedoc.basePath=http://localhost:8080/api #defaults to local server and context path
jsondoc.playgroundEnabled=true
jsondoc.displayMethodAs=URI
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

More interesting, you may also use the provided UI by adding the Livedoc webjar to your classpath:

### Gradle

```groovy 
compile 'org.hildan.livedoc:livedoc-ui-webjar:2.1.1'
```

### Maven

```xml
<dependency>
  <groupId>org.hildan.livedoc</groupId>
  <artifactId>livedoc-ui-webjar</artifactId>
  <version>2.1.1</version>
</dependency>
```

Webjars are supported out-of-the-box by Spring Boot without any additional configuration. You simply need to navigate
 to `/livedoc-ui.html` and enter your JSON doc endpoint URL in the box to fetch your documentation and display it.
 
You may also avoid entering the documentation link each time by providing it as query parameter:

    http://localhost:8080/livedoc-ui.html?url=http://localhost:8080/jsondoc

