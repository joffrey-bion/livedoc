package org.hildan.livedoc.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.internal.impldep.com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

class LivedocPluginExtension(project: Project) {

    val outputFile: Property<File>? = project.objects.property(File::class.java)

    var objectMapper: ObjectMapper? = null
}
