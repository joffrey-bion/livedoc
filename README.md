# Livedoc

[![Maven central version](https://img.shields.io/maven-central/v/org.hildan.livedoc/livedoc-core.svg)](http://mvnrepository.com/artifact/org.hildan.livedoc/livedoc-core)
[![Github Build](https://img.shields.io/github/workflow/status/joffrey-bion/livedoc/CI%20Build?label=build&logo=github)](https://github.com/joffrey-bion/livedoc/actions?query=workflow%3A%22CI+Build%22)
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

Try it yourself! Run the demo:

```
docker run -p 8080:8080 docker.io/hildan/livedoc-demo:latest
```

Then navigate to http://localhost:8080/livedoc/index.html to see what it looks like.
You can see the code directly in this Github repository as the
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
