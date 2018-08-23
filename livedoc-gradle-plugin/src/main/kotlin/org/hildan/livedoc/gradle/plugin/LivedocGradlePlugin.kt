package org.hildan.livedoc.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

open class LivedocGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {

            val ext = project.extensions.create("livedoc", LivedocPluginExtension::class.java)
            project.tasks.create("livedoc", GenerateLivedocTask::class.java) {
                it.outputFile = ext.outputFile
            }

            val compileDeps = configurations.getByName("compileOnly").dependencies
            compileDeps.add(project.dependencies.create())

            afterEvaluate {
                val livedocExt = extensions.findByType(LivedocPluginExtension::class.java)
            }
        }
    }
}
