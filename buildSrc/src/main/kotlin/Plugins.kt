object Plugins {
    val androidApplication = GradlePlugin(id = "com.android.application")
    val kotlinKapt = GradlePlugin(id = "kotlin-kapt")
    val kotlinAndroid = GradlePlugin(id = "kotlin-android")
    val mavenPublish = GradlePlugin(id = "maven-publish")

    val androidLibrary = GradlePlugin(
        id = "com.android.library",
        module = "com.android.tools.build:gradle:${PluginVersions.gradle}"
    )

    val kotlinSerialization = GradlePlugin(
        id = "kotlinx-serialization",
        module = "org.jetbrains.kotlin:kotlin-serialization:${PluginVersions.serialization}"
    )
}
