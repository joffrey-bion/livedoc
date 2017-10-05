# Livedoc

[![Bintray Download](https://img.shields.io/bintray/v/joffrey-bion/maven/livedoc-core.svg)](https://bintray.com/joffrey-bion/maven/livedoc-core/_latestVersion)
[![Maven central version](https://img.shields.io/maven-central/v/org.hildan.livedoc/livedoc-core.svg)](http://mvnrepository.com/artifact/org.hildan.livedoc/livedoc-core)
[![Build Status](https://travis-ci.org/joffrey-bion/livedoc.svg?branch=master)](https://travis-ci.org/joffrey-bion/livedoc)
[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/joffrey-bion/fx-gson/blob/master/LICENSE)

[Livedoc](https://joffrey-bion.github.io/livedoc) is an annotation-based documentation generator for REST and websocket 
services, inspired by the [JSONDoc](http://jsondoc.org) project.

## How does it work?

Livedoc's goal is to generate a documentation of your API from your code. It comes in multiple flavours, depending 
on whether you are developing a standard Spring MVC app, a Spring Boot app, or using another framework.

Livedoc uses reflection to read your annotated classes and output a Java model representing the documentation. In 
most cases, this documentation is serialized to JSON, and may then be used by Livedoc's UI that you can host on your 
server to display a good-looking documentation to your users.

## Get started

Check out the [official documentation](https://joffrey-bion.github.io/livedoc) to get started with Livedoc.

## Credits

Credits to Fabio Maffioletti for his great work on the original JSONDoc project, and to all contributors that helped 
him build JSONDoc up to version 1.2.17, which I started from.

Special thanks to @ST-DDT for his very constructive feedback, both on the original project and on Livedoc.

## License

This library is released under [the MIT license](https://github.com/joffrey-bion/fx-gson/blob/master/LICENSE).
