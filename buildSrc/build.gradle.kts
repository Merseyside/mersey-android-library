plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    with(catalogGradle) {
        implementation(kotlin.gradle)
        implementation(android.gradle)
        implementation(kotlin.serialization)
        implementation(nexusPublish)
        implementation(mersey.gradlePlugins)
    }
}
