object LibraryVersions {

    object Application {
        const val packageName = "com.merseyside.merseyLib"
        const val applicationId = "com.merseyside.merseyLib"
        const val groupId = "com.github.Merseyside"
        const val artifactId = "mersey-android-library"

        const val version = "1.3.1"
        const val versionCode = 131
    }

    object Android {
        const val compileSdk = 28
        const val targetSdk = 29
        const val minSdk = 21
    }

    object Common {
        const val kotlinStdLib = kotlin
        const val coroutines = "1.3.8"
        const val serialization = "1.0.1"
        const val koin = "3.0.0-alpha-4"
    }

    const val kotlin = "1.4.21"
    private const val mokoResources = "0.13.1"

    object Plugins {
        const val gradle = "4.1.1"

        const val kotlin = LibraryVersions.kotlin
        const val serialization = LibraryVersions.kotlin
        const val mokoResources = LibraryVersions.mokoResources
        const val maven = "2.1"
        const val sqlDelight = "1.4.4"
    }

    object Libs {
        object Android {
            const val appCompat = "1.2.0"
            const val annotation = "1.2.0-alpha01"
            const val material = "1.2.1"
            const val coroutines = "1.4.2"
            const val fragment = "1.2.5"
            const val constraintLayout = "2.1.0-alpha2"
            const val lifecycle_extension = "2.2.0"
            const val lifecycle = "2.3.0-rc01"
            const val cardView = "1.0.0"
            const val recyclerView = "1.1.0"
            const val dagger = "2.29.1"
            const val navigation = "2.3.2"
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
            const val rxjava2 = "2.2.20"
            const val coil = "1.1.0"
            const val filemanager = "1.0.3"
            const val typedDataStore = "1.0.0-alpha06"
            const val mokoMvvm = "0.9.1"
        }

        object MultiPlatform {
            const val mokoMvvm = "0.9.1"
            const val mokoResources = LibraryVersions.mokoResources
            const val ktor = "1.5.1"
            const val kodein = "7.1.0"
            const val sqlDelight = "1.4.4"
        }
    }
}