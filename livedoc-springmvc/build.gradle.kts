plugins {
    `java-library`
}

description = "The Spring MVC helper of the Livedoc project"

dependencies {
    api(project(":livedoc-core"))

    implementation("org.reflections:reflections:0.9.10")

    compileOnly("org.jetbrains:annotations:13.0")
    compileOnly("javax.servlet:servlet-api:2.5")
    compileOnly("org.springframework:spring-webmvc:4.3.14.RELEASE")
    compileOnly("org.springframework:spring-messaging:4.3.14.RELEASE")
    compileOnly("org.springframework.data:spring-data-rest-webmvc:2.5.1.RELEASE")

    testImplementation("junit:junit:4.11")
    testImplementation("org.springframework:spring-webmvc:4.3.14.RELEASE")
    testImplementation("org.springframework:spring-messaging:4.3.14.RELEASE")
    testImplementation("org.springframework.data:spring-data-rest-webmvc:2.5.1.RELEASE")
    testImplementation("org.springframework:spring-test:3.1.2.RELEASE")

    testAnnotationProcessor(project(":livedoc-javadoc-processor"))
}
