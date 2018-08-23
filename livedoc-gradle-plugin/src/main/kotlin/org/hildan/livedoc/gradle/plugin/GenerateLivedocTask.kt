package org.hildan.livedoc.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateLivedocTask : DefaultTask() {

    @OutputFile
    var outputFile: Property<File> = project.objects.property(File::class.java)

    init {
        group = "documentation"
        description = "Generates Livedoc JSON documentation for the main source code."
    }

    @TaskAction
    fun generateLivedoc() {
        outputFile.writeText("{ some JSON }")
    }
}
