import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-android")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-Xcontext-receivers"
        )
    }
}