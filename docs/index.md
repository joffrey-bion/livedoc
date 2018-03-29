---
title: Home
layout: default
---

# What is Livedoc ?

Livedoc is a Java library that inspects the Java code of your API and builds its documentation based on what it 
found. It was [inspired by JSONDoc](about-jsondoc), and aims at allowing users to generate a documentation with the 
least possible effort. Ain't nobody's got time to write doc!
 
It does not need you to use a particular framework, although it integrates very well with Spring, because it is able 
to read its annotation and generate doc from that without further configuration.

Moreover, Livedoc is [able to read the Javadoc](javadoc-processing), thus removing the need to use extra annotation.

Last but not least, Livedoc also provides a [UI](livedoc-ui) that you can use to visualize the documentation.

# Get started with Livedoc

Choose your Livedoc flavour:

- [Spring Boot](quickstart/springboot) (recommended)
- [Spring MVC](quickstart/springmvc)
- [Other framework](quickstart/plain)
