package com.decaf.playgroundyandexschool.plugins

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized


class UploadPlugin: Plugin<Project> {

    override fun apply(project: Project) {

        val extension = project.extensions.create("pluginExtension", PluginExtension::class.java)
            ?: throw GradleException("extentions required")

        val androidComponents = project.extensions.findByType(AndroidComponentsExtension::class.java)
            ?: throw GradleException("'com.android.application' plugin required.")

        val android = project.extensions.findByType(BaseAppModuleExtension::class.java)
            ?: throw GradleException("'com.android.application' plugin required.")

        androidComponents.onVariants { variant ->
            val capVariantName = variant.name.capitalized()
            val apkDit = variant.artifacts.get(SingleArtifact.APK)


            val uploadTask = project.tasks.register("uploadApkFor$capVariantName", UploadTask::class.java) {
                apkDir.set(apkDit)
                fileName.set("todolist-${variant.name}-${android.defaultConfig.versionName}.apk")
            }

            val checkSizeTask = project.tasks.register("checkSizeFor$capVariantName", ApkValidateTask::class.java){
                enabled = extension.enableSizeCheck.get()
                apkDir.set(apkDit)
                maxSize.set(extension.fileSizeLimitInMb.get())
            }

            uploadTask.configure {
                dependsOn(checkSizeTask)
            }
        }
    }
}