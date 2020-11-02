object LibraryVersions {

    object Application {
        const val packageName = "com.merseyside.merseyLib"
        const val applicationId = "com.merseyside.merseyLib"
    }

    object Android {
        const val compileSdk = 28
        const val targetSdk = 29
        const val minSdk = 17

        const val version = "1.2.6"
        const val versionCode = 126
    }

    object Common {
        const val kotlinStdLib = kotlin
        const val coroutines = "1.3.8"
        const val serialization = "1.0.0"
    }

    const val kotlin = "1.4.20-M1"
    private const val mokoResources = "0.13.1"

    object Plugins {
        const val android = "4.0.0"

        const val kotlin = LibraryVersions.kotlin
        const val serialization = LibraryVersions.kotlin
        const val androidExtensions = LibraryVersions.kotlin
        const val mokoResources = LibraryVersions.mokoResources
        const val maven = "2.1"
        const val sqlDelight = "1.3.0"
    }

    object Libs {
        object Android {
            const val appCompat = "1.2.0"
            const val annotation = "1.1.0"
            const val material = "1.2.1"
            const val fragment = "1.2.5"
            const val constraintLayout = "2.0.4"
            const val lifecycle = "2.2.0"
            const val cardView = "1.0.0"
            const val recyclerView = "1.1.0"
            const val dagger = "2.29.1"
            const val navigation = "2.3.1"
            const val paging = "2.1.2"
            const val billing = "3.0.1"
            const val publisher = "v3-rev142-1.25.0"
            const val auth = "0.22.0"
            const val firebaseFirestore = "22.0.0"
            const val playCore = "1.8.3"
            const val keyboard = "2.3.0"
            const val gson = "2.8.6"
            const val worker = "2.4.0"
            const val room = "2.2.5"
            const val coil = "1.0.0"
        }

        object MultiPlatform {
            const val mokoMvvm = "0.8.0"
            const val mokoResources = LibraryVersions.mokoResources
            const val ktor = "1.4.1"
            const val kodein = "7.1.0"
            const val sqlDelight = "1.4.4"
        }
    }
}