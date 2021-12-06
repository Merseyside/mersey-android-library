buildscript {
    repositories {
        gradlePluginPortal()
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(common.plugins.versionChecker)
    `nexus-config`
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()

        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://raw.githubusercontent.com/guardianproject/gpmaven/master") }
        maven { url = uri("https://maven.fabric.io/public") }
    }

    configurations.configureEach {
        resolutionStrategy {
            val coroutines: MinimalExternalModuleDependency =
                rootProject.androidLibs.coroutines.get()
            val forcedCoroutines: ModuleVersionSelector =
                org.gradle.api.internal.artifacts.DefaultModuleVersionSelector.newSelector(
                    coroutines.module,
                    coroutines.versionConstraint.requiredVersion
                )
            force(forcedCoroutines)
        }
    }
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}