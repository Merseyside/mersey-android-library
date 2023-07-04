@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(common.plugins.versionChecker)
}

allprojects {
    plugins.withId("org.gradle.maven-publish") {
        group = "io.github.merseyside"
        version = "2.0.7"
    }
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}