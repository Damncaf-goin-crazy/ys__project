package com.decaf.playgroundyandexschool.plugins

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentDisposition
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File

private const val BASE_URL = "https://api.telegram.org"

class TelegramApi(
    private val httpClient: HttpClient
) {
    private val token = "7311212309:AAFPQ2hf9CcfT46E4sypOYJ5LgL-q5tO-3I"
    private val chatId = "1273936283"

    suspend fun uploadFile(file: File, fileName: String) {

        val response = httpClient.post("$BASE_URL/bot${token}/sendDocument") {
            parameter("chat_id", chatId)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("document", file.readBytes(), Headers.build {
                            append(
                                HttpHeaders.ContentDisposition,
                                "${ContentDisposition.Parameters.FileName}=\"${fileName}\""
                            )
                        })
                    }
                )
            )
        }
        println("Code = ${response.status.value}")
        println("Desc = $response")

    }

    suspend fun sendMessage(text: String) {
        val response = httpClient.post("$BASE_URL/bot${token}/sendMessage") {
            parameter("chat_id", chatId)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("text", text)
                    }
                )
            )
        }

        println("Code = ${response.status.value}")
        println("Desc = $response")
    }

}