import org.hildan.github.changelog.builder.DEFAULT_EXCLUDED_LABELS

plugins {
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0"
    id("org.hildan.github.changelog") version "1.6.0"
}

allprojects {
    group = "org.hildan.livedoc"
}

val buildDate = java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.now())

val githubUser = findProperty("githubUser") as String? ?: System.getenv("GITHUB_USER")
val githubSlug = "$githubUser/${rootProject.name}"
val githubRepoUrl = "https://github.com/$githubSlug"

changelog {
    futureVersionTag = project.version.toString()
    sinceTag = "v0.1.0"
    // these tags were experiments
    skipTags = listOf("v0.4.1", "v0.4.2", "v0.4.3")
    excludeLabels = listOf("wontfix", "duplicate", "invalid", "internal", "meta / documentation", "question")
}

subprojects {
    apply(plugin = "checkstyle")

    repositories {
        mavenCentral()
        jcenter()
    }

    configurations {
        register("checkstyleConfig")
    }

    dependencies {
        "checkstyleConfig"("org.hildan.checkstyle:checkstyle-config:2.3.0")
    }

    configure<CheckstyleExtension> {
        maxWarnings = 0
        toolVersion = "8.8"
        config = resources.text.fromArchiveEntry(configurations["checkstyleConfig"], "checkstyle.xml")
    }
}

tasks.register("syncVersion")

configure(subprojects.filter { it.name != "sample-app" }) {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    tasks.withType<ProcessResources> {
        val expandProps = mapOf("projectVersion" to project.version, "buildDate" to buildDate)
        inputs.properties(expandProps)
        filesMatching("**/*.properties") {
            expand(expandProps)
        }
    }

    tasks.withType<Jar> {
        manifest {
            attributes(
                mapOf(
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version,
                    "Specification-Title" to project.name,
                    "Specification-Version" to project.version,
                    "Build-Date" to buildDate
                )
            )
        }
    }

    afterEvaluate {

        configure<JavaPluginExtension> {
            withJavadocJar()
            withSourcesJar()
        }

        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("maven") {
                    from(components["java"])
                    configurePomForMavenCentral(project)
                }
            }
        }

        configure<SigningExtension> {
            val signingKey: String? by project
            val signingPassword: String? by project
            useInMemoryPgpKeys(signingKey, signingPassword)
            sign(extensions.getByType<PublishingExtension>().publications)
        }
    }
}

nexusPublishing {
    packageGroup.set("org.hildan")
    repositories {
        sonatype()
    }
}

fun MavenPublication.configurePomForMavenCentral(project: Project) = pom {
    name.set(project.name)
    description.set(project.description)
    url.set(githubRepoUrl)
    licenses {
        license {
            name.set("The MIT License")
            url.set("https://opensource.org/licenses/MIT")
        }
    }
    developers {
        developer {
            id.set("joffrey-bion")
            name.set("Joffrey Bion")
            email.set("joffrey.bion@gmail.com")
        }
    }
    scm {
        connection.set("scm:git:$githubRepoUrl.git")
        developerConnection.set("scm:git:git@github.com:$githubSlug.git")
        url.set(githubRepoUrl)
    }
}
