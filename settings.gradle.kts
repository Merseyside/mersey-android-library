enableFeaturePreview("GRADLE_METADATA")

//pluginManagement {
//    repositories {
//        jcenter()
//        google()
//        maven { url = uri("https://dl.bintray.com/kotlin/kotlin") }
//        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
//        maven { url = uri("https://jetbrains.bintray.com/kotlin-native-dependencies") }
//        maven { url = uri("https://maven.fabric.io/public") }
//        maven { url = uri("https://dl.bintray.com/icerockdev/plugins") }
//        maven { url = uri("https://plugins.gradle.org/m2/") }
//    }
//}

val properties = startParameter.projectProperties
// ./gradlew -PlibraryPublish :mvvm:publishToMavenLocal
val libraryPublish: Boolean = properties.containsKey("libraryPublish")

//if (!libraryPublish) {
    include(":animators")
    include(":clean-mvvm-arch")
    include(":adapters")
    include(":utils")
    include(":app")
//}

include(":kmp-clean-mvvm-arch")
include(":kmp-utils")

rootProject.name = "mersey-library"