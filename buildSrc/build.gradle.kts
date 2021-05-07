plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.serialization") version "1.5.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

val kotlin = "1.5.0"
val gradle = "4.1.3"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
    implementation("com.android.tools.build:gradle:$gradle")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlin")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
