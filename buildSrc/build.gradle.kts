plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.5.30-RC"
    kotlin("plugin.serialization") version "1.5.30-RC"
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

val kotlin = "1.5.30-RC"
val gradle = "4.2.1"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
    implementation("com.android.tools.build:gradle:$gradle")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlin")
}
