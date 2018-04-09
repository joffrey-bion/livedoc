# Livedoc

[![Bintray Download](https://img.shields.io/bintray/v/joffrey-bion/maven/livedoc-core.svg)](https://bintray.com/joffrey-bion/maven/livedoc-core/_latestVersion)
[![Maven central version](https://img.shields.io/maven-central/v/org.hildan.livedoc/livedoc-core.svg)](http://mvnrepository.com/artifact/org.hildan.livedoc/livedoc-core)
[![Build Status](https://travis-ci.org/joffrey-bion/livedoc.svg?branch=master)](https://travis-ci.org/joffrey-bion/livedoc)
[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/joffrey-bion/fx-gson/blob/master/LICENSE)

[Livedoc](https://joffrey-bion.github.io/livedoc) is a documentation generator for REST and websocket services, 
inspired by the [JSONDoc](http://jsondoc.org) project. 

Livedoc tries to read all the doc from the code and the Javadoc. Annotations are considered a failure of Livedoc to 
grasp an aspect of the code. They should mostly be used to add what Livedoc couldn't read from the code, or override a
 piece of documentation that does not match the user's needs.
 
This is one of the reasons why it should be quick to add Livedoc to your project.

## Get started

Check out the [official documentation](https://joffrey-bion.github.io/livedoc) to get started with Livedoc.

## Livedoc UI Demo

Check out the [live demo of Livedoc's UI](http://livedoc-demo.hildan.org:8080/livedoc/index.html) to see what it looks 
like. You can see the code directly in this Github repository as the
 [sample-app subproject](https://github.com/joffrey-bion/livedoc/tree/master/sample-app).

## Credits

Credits to [@fabiomaffioletti](https://github.com/fabiomaffioletti) for his great work on the original JSONDoc project, 
and to all contributors that helped him build JSONDoc up to version 1.2.17, which I started from.

Special thanks to [@ST-DDT](https://github.com/ST-DDT) for his very constructive feedback, both on the original project 
and on Livedoc.

Livedoc uses the [therapi-runtime-javadoc](https://github.com/dnault/therapi-runtime-javadoc) library by 
[@dnault](https://github.com/dnault) for its Javadoc-related processing.

## License

This library is released under [the MIT license](https://github.com/joffrey-bion/fx-gson/blob/master/LICENSE).
