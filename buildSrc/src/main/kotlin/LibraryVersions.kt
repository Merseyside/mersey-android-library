object LibraryVersions {

    object Android {
        const val compileSdk = 28
        const val targetSdk = 29
        const val minSdk = 17

        const val version = "1.2.4"
        const val versionCode = 124
    }

    const val kotlin = "1.3.72"

    private const val mokoResources = "0.9.0"

    object Plugins {
        const val android = "4.0.0"

        const val kotlin = LibraryVersions.kotlin
        const val serialization = LibraryVersions.kotlin
        const val androidExtensions = LibraryVersions.kotlin
        const val mokoResources = LibraryVersions.mokoResources
        const val maven = "2.1"
    }

    object Libs {
        object Android {
            const val kotlinStdLib = LibraryVersions.kotlin
            const val coroutines = "1.3.7"
            const val appCompat = "1.1.0"
            const val material = "1.2.0-alpha05"
            const val fragment = "1.2.4"
            const val constraintLayout = "1.1.3"
            const val lifecycle = "2.0.0"
            const val cardView = "1.0.0"
            const val recyclerView = "1.0.0"
            const val dagger = "2.27"
            const val navigation = "2.2.1"
            const val paging = "1.0.1"
            const val billing = "2.2.0"
            const val publisher = "v3-rev142-1.25.0"
            const val auth = "0.20.0"
            const val firebaseFirestore = "21.4.3"
            const val playCore = "1.7.2"
            const val keyboard = "2.3.0"
            const val gson = "2.8.6"
            const val worker = "2.3.4"
            const val room = "2.0.0"

            object MerseyLibs {
                const val version = "1.2.1"
            }
        }

        object MultiPlatform {
            const val kotlinStdLib = LibraryVersions.kotlin

            const val coroutines = "1.3.5"
            const val serialization = "0.20.0"
            const val mokoMvvm = "0.6.0"
            const val mokoResources = LibraryVersions.mokoResources
            const val ktor = "1.3.2"

            const val kodein = "6.5.5"
            const val sqlDelight = "1.3.0"
        }
    }
}