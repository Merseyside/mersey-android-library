enableFeaturePreview("GRADLE_METADATA")

pluginManagement {
    repositories {
        jcenter()
        google()
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        maven { url = uri("https://jetbrains.bintray.com/kotlin-native-dependencies") }
        maven { url = uri("https://maven.fabric.io/public") }
        maven { url = uri("https://dl.bintray.com/icerockdev/plugins") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }

    resolutionStrategy.eachPlugin {
        // part of plugins defined in Deps.Plugins, part in buildSrc/build.gradle.kts
        val module = LibraryDeps.plugins[requested.id.id] ?: return@eachPlugin

        useModule(module)
    }
}

val properties = startParameter.projectProperties
// ./gradlew -PlibraryPublish :mvvm:publishToMavenLocal
val libraryPublish: Boolean = properties.containsKey("libraryPublish")

//if (!libraryPublish) {
    include(LibraryModules.Android.animators)
    include(LibraryModules.Android.cleanMvvmArch)
    include(LibraryModules.Android.adapters)
    include(LibraryModules.Android.utils)
    include(":app")
//}

include(LibraryModules.MultiPlatform.cleanMvvmArch.name)
include(LibraryModules.MultiPlatform.utils.name)

rootProject.name = "mersey-library"