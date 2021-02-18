plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.serialization") version "1.4.30"
}

repositories {
    mavenLocal()
    jcenter()
    google()
}

val kotlin = "1.4.30"
val gradle = "4.1.1"
val mavenVersion = "2.1"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin")
    implementation("com.android.tools.build:gradle:$gradle")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlin")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
