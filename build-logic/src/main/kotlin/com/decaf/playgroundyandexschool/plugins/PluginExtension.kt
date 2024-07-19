package com.decaf.playgroundyandexschool.plugins

import org.gradle.api.provider.Property

interface PluginExtension {

    val fileSizeLimitInMb: Property<Int>

    val enableSizeCheck: Property<Boolean>

}