package com.decaf.playgroundyandexschool.plugins
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class UploadTask : DefaultTask() {


    @get:Input
    abstract val fileName: Property<String>

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @TaskAction
    fun upload() {
        val api = TelegramApi(HttpClient(OkHttp))

        runBlocking {
            apkDir.get().asFile.listFiles()
                ?.filter { it.name.endsWith(".apk") }
                ?.forEach {
                    println("FILE = ${it.absolutePath}")
                    api.uploadFile(it, fileName.get())
                }
        }

    }
}