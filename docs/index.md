---
title: Home
layout: default
---

## What is Livedoc ?

Livedoc is a Java library that inspects the Java code of your API and builds its documentation based on what it 
found. It was [inspired by JSONDoc](about-jsondoc), and aims at allowing users to generate a documentation with the 
least possible effort. Ain't nobody's got time to write doc!
 
It does not need you to use a particular framework, although it integrates very well with Spring, because it is able 
to read its annotation and generate doc from that without further configuration.

Moreover, Livedoc is [able to read the Javadoc](javadoc-processing), thus removing the need to use extra annotation.

Last but not least, Livedoc also provides a [UI](livedoc-ui) that you can use to visualize the documentation.

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
