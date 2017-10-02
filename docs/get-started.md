---
title: Get Started
layout: default
---

# Get started with Livedoc

## Pure Livedoc

`livedoc-core` allows you to annotate any Java code and generate an Java representation of the documentation. You 
may then do what you please with it, such as serializing it to JSON.

Add a dependency on `livedoc-core`:

```groovy
dependencies {
    compile 'org.hildan.livedoc:livedoc-core:2.1.1'
}
```

## Spring MVC

`livedoc-springmvc` knows Spring annotations and is able to generate a documentation without a single Livedoc 
annotation. Of course, it is recommended to add Livedoc annotations in order to provide more details about the API. 
Moreover, `livedoc-springmvc` provides a `JsonLivedocController` to return the documentation as JSON at the 
`/jsondoc` endpoint.

Add a dependency on `livedoc-springmvc`:

```groovy
dependencies {
    compile 'org.hildan.livedoc:livedoc-springmvc:2.1.1'
}
```

## Spring Boot

`livedoc-springboot` is almost identical to the Spring MVC flavour, but you have even less work to do: a simple 
configuration in your `application.properties` and you're good to go!

Add a dependency on `livedoc-springboot`:

```groovy
dependencies {
    compile 'org.hildan.livedoc:livedoc-springboot:2.1.1'
}
```

