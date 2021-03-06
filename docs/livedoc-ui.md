# Livedoc UI

Livedoc UI is a react UI that nicely displays the documentation generated by Livedoc, and provides a playground to 
perform some test requests.

## Prerequisites

To use to provided UI to view your doc, you first need to have some service that returns the documentation as JSON.

If you're using one of the Spring-flavoured versions of Livedoc, this is provided out of the box thanks to the 
provided doc controller. 
Otherwise, you need to provide this service yourself. It needs to return a JSON with the same shape as a 
`org.hildan.livedoc.core.model.doc.Livedoc` object.

## Embed the UI in your webapp with a webjar

To do so, simply add the Livedoc UI webjar to your classpath:

{% include dependency.md artifactId="livedoc-ui-webjar" %}

Spring Boot automatically serves content from webjars without any additional configuration. If you're not using Spring 
boot and you're not familiar with webjars, this [baeldung post](http://www.baeldung.com/maven-webjars) explains how to
use one in a Spring MVC application.

If you're not using Spring at all, check the documentation of your framework to see if it supports webjars. 
Otherwise, you will have to either serve the UI with docker (see below) or extract the content of the jar and serve the 
static files yourself.
 
To view the generated doc, simply navigate to `/livedoc/index.html` and enter your JSON doc URL in the box to fetch 
your documentation and display it.

You may also avoid entering the JSON documentation url each time by providing it as the `url` query parameter like 
this:

    http://your.host/livedoc/index.html?url=http://your.host/jsondoc

## Serve the UI with Docker

Livedoc UI is also provided as a tiny public docker image which serves the UI static files using nginx:

    docker run -p 80:80 hildan/livedoc-ui
    
It makes the UI accessible at:

    http://your.host/index.html

You may also avoid entering the JSON documentation url each time by providing it as the `url` query parameter like 
this:

    http://your.doc.host/index.html?url=http://your.app.host/jsondoc
