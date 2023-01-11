buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(common.plugins.versionChecker)
    `nexus-config`
}

allprojects {
    group = "io.github.merseyside"
    version = "2.0.6"
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}