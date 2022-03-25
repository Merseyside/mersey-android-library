plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    gradlePluginPortal()
}

val kotlin = "1.6.20-RC2"
val gradle = "7.3.0-alpha05"
val nexus = "1.1.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
    implementation("com.android.tools.build:gradle:$gradle")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlin")
    implementation("io.github.gradle-nexus:publish-plugin:$nexus")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
}
