package com.decaf.playgroundyandexschool.plugins

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class ApkValidateTask : DefaultTask() {


    @get:Input
    abstract val maxSize: Property<Int>

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @TaskAction
    fun checkSize() {
        val api = TelegramApi(HttpClient(OkHttp))

        runBlocking {
            apkDir.get().asFile.listFiles()
                ?.filter { it.name.endsWith(".apk") }
                ?.forEach {
                    println("FILE = ${it.absolutePath}")

                    val size = it.length() / (1024 * 1024)
                    println("size = $size")
                    if (size > maxSize.get()) {
                        api.sendMessage("Error! Size of current building apk file is $size, but should be < ${maxSize.get()}")
                        throw GradleException("Size of current building apk file is $size, but should be < ${maxSize.get()}")
                    } else {
                        api.sendMessage("Size of current building apk file is $size")
                    }
                }
        }

    }
}