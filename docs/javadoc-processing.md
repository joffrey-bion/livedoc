# Javadoc Processing

With Livedoc, it is possible to generate a documentation with almost no configuration, by directly reading 
descriptions from the relevant Javadoc. 

This is possible thanks to the [therapi-runtime-javadoc](https://github.com/dnault/therapi-runtime-javadoc) library, 
which allows Livedoc's processor to include the Javadoc inside the classes at compile time, thus enabling access during 
the doc generation at runtime.

## Enable Javadoc processing

To enable javadoc processing, you'll need to add Livedoc's javadoc processor on the processor classpath during the 
build of your project.

It can be done this way:

{% include dependency-processor.md artifactId="livedoc-javadoc-processor" %}

This is all you need to do. Livedoc will then find the Javadoc and use it to get methods and parameter descriptions.
