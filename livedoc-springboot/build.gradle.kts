plugins {
    `java-library`
}

description = "The Spring Boot helper of the Livedoc project"

dependencies {
    api(project(":livedoc-springmvc"))

    compileOnly("org.springframework.boot:spring-boot-starter-web:1.5.7.RELEASE")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:1.5.7.RELEASE")
}
