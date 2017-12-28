# Using Livedoc UI

## Prerequisites

To use to provided UI to view your doc, you first need to have some service that returns the documentation as JSON. If 
you're using one of the Spring-flavoured versions of Livedoc, this is provided out of the box. Otherwise, you need to
provide this service yourself.

## How to

Then, simply add the Livedoc UI webjar to your classpath:

{% include dependency.md artifactId="livedoc-ui-webjar" %}

Spring Boot automatically serves content from webjars without any additional configuration. If you're not using Spring 
boot and you're not familiar with webjars, this [baeldung post](http://www.baeldung.com/maven-webjars) explains how to
use one in a Spring application. If you're not using Spring at all, you might have to extract the content of the jar 
and serve the index file and the JavaScript yourself.
 
To view the generated doc, simply navigate to `/livedoc/index.html` and enter your JSON doc URL in the box to fetch 
your documentation and display it.

You may also avoid entering the JSON documentation url each time by providing it as the `url` query parameter like 
this:

    http://localhost:8080/livedoc/index.html?url=http://localhost:8080/jsondoc
