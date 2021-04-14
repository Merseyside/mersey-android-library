object LibraryVersions {

    object Application {
        const val groupId = "com.github.Merseyside"
        const val artifactId = "mersey-android-library"

        const val version = "1.3.2"
        const val versionCode = 132

        const val compileSdk = 28
        const val targetSdk = 29
        const val minSdk = 21
    }

    object Common {
        const val kotlinStdLib = kotlin
        const val coroutines = "1.4.2"
        const val serialization = "1.1.0-RC"
    }

    const val kotlin = "1.4.30"

    object Plugins {
        const val gradle = "4.1.1"

        const val kotlin = LibraryVersions.kotlin
        const val serialization = LibraryVersions.kotlin
        const val maven = "2.1"
    }

    object Libs {
        const val appCompat = "1.2.0"
        const val activity = "1.2.0"
        const val annotation = "1.2.0-alpha01"
        const val material = "1.3.0"
        const val coroutines = "1.4.2"
        const val fragment = "1.3.0"
        const val constraintLayout = "2.1.0-alpha2"
        const val lifecycle = "2.3.0"
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
    }
}