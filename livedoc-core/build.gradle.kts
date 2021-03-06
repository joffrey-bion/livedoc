plugins {
    `java-library`
}

description = "The core implementation of the Livedoc project"

dependencies {
    api(project(":livedoc-annotations"))

    implementation("org.hildan.generics:generics-explorer:1.1.0")
    implementation("com.github.therapi:therapi-runtime-javadoc:0.6.0")
    implementation("org.reflections:reflections:0.9.10")
    implementation("org.freemarker:freemarker:2.3.20")
    implementation("org.slf4j:slf4j-api:1.7.10")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.6.5")

    compileOnly("org.jetbrains:annotations:13.0")
    compileOnly("org.hibernate:hibernate-validator:5.1.3.Final")

    testAnnotationProcessor(project(":livedoc-javadoc-processor"))

    testImplementation("junit:junit:4.11")
    testImplementation("org.hibernate:hibernate-validator:5.1.3.Final")
}
