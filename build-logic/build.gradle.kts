plugins {
    `kotlin-dsl`
}

gradlePlugin{
    plugins.register("tg-plugin"){
        id = "tg-plugin"
        implementationClass = "com.decaf.playgroundyandexschool.plugins.UploadPlugin"
    }
}

dependencies {
    implementation(libs.agp)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.bundles.ktor)
}