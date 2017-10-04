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
String apiVersion = "1.0";
String basePath = "http://mycompany.com/api";
boolean playgroundEnabled = true;
MethodDisplay displayMethodAs = MethodDisplay.URI;

List<String> packages = new ArrayList<>();
pakcages.add("com.mycompany.controllers"); // packages in which you have your spring controllers
packages.add("com.mycompany.model"); // packages in which you have your model classes
packages.add("org.example.external.model"); // they can also belong to external jars

LivedocReader livedocReader = LivedocReader.basicAnnotationReader(packages);
Livedoc livedoc = livedocReader.read(apiVersion, basePath, playgroundEnabled, displayMethodAs);
```

If you want to customize how the `LivedocReader` inspects your classes, you may use a `LivedocReaderBuilder` to 
create it.

## Use the documentation

The `Livedoc` Java object holds all the documentation of your API and classes. You may now use it as you please.

For instance, you may serialize it as JSON and write it to a file, or you may also return it from one of your own 
controllers.

If your framework allows it, you may add the Livedoc UI webjar to your classpath in order to view your documentation 
in a nice UI:

{% include dependency.md artifactId="livedoc-ui-webjar" %}

Simply serve `livedoc-ui.html`, which is part of the resources inside the webjar, and you are good to go.
