@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(common.plugins.versionChecker)
    //`nexus-config`
}

allprojects {
    plugins.withId("org.gradle.maven-publish") {
        group = "io.github.merseyside"
        version = androidLibs.versions.mersey.android.get()
    }
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}