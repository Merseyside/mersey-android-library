plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

val kotlin = "1.6.0"
val gradle = "7.0.3"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
    implementation("com.android.tools.build:gradle:$gradle")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlin")
}
