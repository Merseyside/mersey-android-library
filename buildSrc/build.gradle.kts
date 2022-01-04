plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    gradlePluginPortal()
}

val kotlin = "1.6.10"
val gradle = "7.0.4"
val nexus = "1.1.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
    implementation("com.android.tools.build:gradle:$gradle")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlin")
    implementation("io.github.gradle-nexus:publish-plugin:$nexus")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$kotlin")
}
