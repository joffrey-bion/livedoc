plugins {
    `java-library`
}

description = "The annotation processor to allow Livedoc to read Javadoc at runtime"

dependencies {
    implementation("com.github.therapi:therapi-runtime-javadoc-scribe:0.6.0")
}
