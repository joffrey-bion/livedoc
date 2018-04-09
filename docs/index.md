---
title: Home
layout: default
---

## What is Livedoc ?

Livedoc is a Java library that inspects the Java code of your API and builds its documentation based on what it 
found. It was [inspired by JSONDoc](about-jsondoc), and aims at allowing users to generate a documentation with the 
least possible effort. Ain't nobody's got time to write doc!

## Design Goals

#### Write less
The code never lies, handwritten docs do.
That's why most of the doc must come from the code, and if not, then from the Javadoc.
Using Livedoc's annotations is considered a failure of Livedoc to grasp an aspect of your code.

#### Stay Live
Parts of the configuration of your app happens at runtime, that's why Livedoc generates the doc from running code.

#### Just work
Livedoc just works with as little configuration as possible. It is almost completely configurable, but most of the 
decisions have already been made for you to get the best of Livedoc in most situations.

## Features

#### Spring integration
Livedoc does not require you to use a particular framework, but it integrates very well with Spring. It understands 
Spring's annotations and generates the doc from that without further configuration. Using Spring is certainly the way
 to make the most out of Livedoc.

#### Javadoc aware
Livedoc is [able to read the Javadoc](javadoc-processing), thus removing the need to use extra annotations.

#### UI
Livedoc also provides a [UI](livedoc-ui) that you can use to visualize the documentation.

Check out the [live demo of Livedoc's UI](http://livedoc-demo.hildan.org:8080/livedoc/index.html) to see what it 
looks like. You can see the code directly in this Github repository as the
[sample-app subproject](https://github.com/joffrey-bion/livedoc/tree/master/sample-app).

## Get started with Livedoc

Choose your Livedoc flavour:

- [Spring Boot](quickstart/springboot) (recommended)
- [Spring MVC](quickstart/springmvc)
- [Other framework](quickstart/plain)

Then you may then check out:
 
- how to read some documentation [from the javadoc](javadoc-processing)
- how to serve the documentation with [the provided UI](livedoc-ui)

## Credits

Credits to [@fabiomaffioletti](https://github.com/fabiomaffioletti) for his great work on the original JSONDoc project, 
and to all contributors that helped him build JSONDoc up to version 1.2.17, which I started from.

Special thanks to [@ST-DDT](https://github.com/ST-DDT) for his very constructive feedback, both on the original project 
and on Livedoc.

Livedoc uses the [therapi-runtime-javadoc](https://github.com/dnault/therapi-runtime-javadoc) library by 
[@dnault](https://github.com/dnault) for its Javadoc-related processing.
