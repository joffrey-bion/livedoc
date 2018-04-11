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

## Use the provided controller via XML

`livedoc-springmvc` provides a `JsonLivedocController` class that exposes the documentation as JSON at the `/jsondoc`
 endpoint.

```xml
<bean id="documentationController" class="org.hildan.livedoc.springmvc.controller.JsonLivedocController">
    <constructor-arg name="version" value="1.0" />
    <constructor-arg name="packages">
        <list>
            <value>com.mycompany.controllers</value> <!-- packages in which you have your spring controllers -->
            <value>com.mycompany.model</value> <!-- packages in which you have your model classes -->
            <value>org.example.external.model</value> <!-- they can also belong to external jars -->
        </list>
    </constructor-arg>
    <property name="baseUrl" value="http://localhost:8080/api" /> <!-- optional -->
    <property name="playgroundEnabled" value="true" /> <!-- optional -->
    <property name="displayMethodAs" value="URI" /> <!-- optional -->
</bean>
```

## Annotate your code

The beauty of the Spring flavour is that you don't need to annotate everything, because Livedoc already knows Spring 
annotations and can generate a doc from them.
That being said, you probably want description of your fields and classes, and that's why you will still need some 
Livedoc annotations.

## Access the documentation

By navigating to the `/jsondoc` endpoint on your server, you may get the JSON description of your documentation.

More interesting, you may also use the provided UI with [very few steps](../livedoc-ui).
