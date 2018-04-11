---
title: Quickstart Standalone
layout: default
---

# Quickstart: Plain Livedoc

`livedoc-core` allows you to annotate any Java code and generate an Java representation of the documentation. You 
may then do what you please with it, such as serializing it to JSON.

## Add a dependency on `livedoc-core`

{% include dependency.md artifactId="livedoc-core" %}

## Annotate your code

Now that you have the Livedoc dependency, you may use Livedoc annotations to add documentation to your project.

> Note: in the standalone version of Livedoc, you need to annotate everything you want documented.
If you want to have out-of-the-box detection of your controllers and types, you need to use Spring and the Spring 
flavour of Livedoc.

## Generate the documentation

To generate the documentation, you need a `LivedocReader` to inspect your code. An easy way of creating one is to use
 the `LivedocReader.basicAnnotationReader(packages)` method:
 
 ```java
ApiMetaData apiInfo = new ApiMetaData();
apiInfo.setVersion("1.0");
apiInfo.setBaseUrl("http://mycompany.com/api");

LivedocConfiguration config = new LivedocConfiguration();
config.setPlaygroundEnabled(true);
config.setDisplayMethodAs(MethodDisplay.URI);

List<String> packages = new ArrayList<>();
pakcages.add("com.mycompany.controllers"); // packages in which you have your spring controllers
packages.add("com.mycompany.model"); // packages in which you have your model classes
packages.add("org.example.external.model"); // they can also belong to external jars

LivedocReader livedocReader = LivedocReader.basicAnnotationReader(packages);
Livedoc livedoc = livedocReader.read(apiInfo, config);
```

If you want to customize how the `LivedocReader` inspects your classes, you may use a `LivedocReaderBuilder` to 
create it.

## Use the documentation

The `Livedoc` Java object holds all the documentation of your API and classes. Once you have built this object as 
mentioned in the previous section, you may use it as you please.

For instance, you may serialize it as JSON and write it to a file. You may also return it from one of your own 
controllers, which is basically what the Spring flavour of Livedoc provides out of the box.

If your framework supports webjars, you may also use [Livedoc's UI](../livedoc-ui) to view the doc.
