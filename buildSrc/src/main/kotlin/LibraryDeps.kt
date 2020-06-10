object LibraryDeps {
    object Plugins {
        const val kotlinSerialization =
            "org.jetbrains.kotlin:kotlin-serialization:${LibraryVersions.Plugins.serialization}"
        const val androidExtensions =
            "org.jetbrains.kotlin:kotlin-android-extensions:${LibraryVersions.Plugins.androidExtensions}"
        const val mokoResources =
            "dev.icerock.moko:resources-generator:${LibraryVersions.Plugins.mokoResources}"
    }

    object Libs {
        object Android {
            val kotlinStdLib = AndroidLibrary(
                name = "org.jetbrains.kotlin:kotlin-stdlib:${LibraryVersions.Libs.Android.kotlinStdLib}"
            )
            val coroutinesCore = AndroidLibrary(
                name = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${LibraryVersions.Libs.Android.coroutines}"
            )
            val coroutines = AndroidLibrary(
                name = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${LibraryVersions.Libs.Android.coroutines}"
            )
            val appCompat = AndroidLibrary(
                name = "androidx.appcompat:appcompat:${LibraryVersions.Libs.Android.appCompat}"
            )
            val material = AndroidLibrary(
                name = "com.google.android.material:material:${LibraryVersions.Libs.Android.material}"
            )
            val fragment = AndroidLibrary(
                name = "androidx.fragment:fragment:${LibraryVersions.Libs.Android.fragment}"
            )
            val recyclerView = AndroidLibrary(
                name = "androidx.recyclerview:recyclerview:${LibraryVersions.Libs.Android.recyclerView}"
            )
            val constraintLayout = AndroidLibrary(
                name = "androidx.constraintlayout:constraintlayout:${LibraryVersions.Libs.Android.constraintLayout}"
            )
            val lifecycle = AndroidLibrary(
                name = "androidx.lifecycle:lifecycle-extensions:${LibraryVersions.Libs.Android.lifecycle}"
            )
            val cardView = AndroidLibrary(
                name = "androidx.cardview:cardview:${LibraryVersions.Libs.Android.cardView}"
            )
            val annotation = AndroidLibrary(
                name = "androidx.annotation:annotation:${LibraryVersions.Libs.Android.appCompat}"
            )
            val paging = AndroidLibrary(
                name = "android.arch.paging:runtime:${LibraryVersions.Libs.Android.paging}"
            )
            val reflect = AndroidLibrary(
                name = "org.jetbrains.kotlin:kotlin-reflect:${LibraryVersions.Libs.Android.kotlinStdLib}"
            )
            val playCore = AndroidLibrary(
                name = "com.google.android.play:core:${LibraryVersions.Libs.Android.playCore}"
            )
            val billing = AndroidLibrary(
                name = "com.android.billingclient:billing:${LibraryVersions.Libs.Android.billing}"
            )
            val billingKtx = AndroidLibrary(
                name = "com.android.billingclient:billing-ktx:${LibraryVersions.Libs.Android.billing}"
            )
            val publisher = AndroidLibrary(
                name = "com.google.apis:google-api-services-androidpublisher:${LibraryVersions.Libs.Android.publisher}"
            )
            val firebaseFirestore = AndroidLibrary(
                name = "com.google.firebase:firebase-firestore-ktx:${LibraryVersions.Libs.Android.firebaseFirestore}"
            )
            val oauth2 = AndroidLibrary(
                name = "com.google.auth:google-auth-library-oauth2-http:${LibraryVersions.Libs.Android.auth}"
            )
            val room = AndroidLibrary(
                name = "android.arch.persistence.room:rxjava2:${LibraryVersions.Libs.Android.room}"
            )
            val roomCompiler = KaptLibrary(
                name = "android.arch.persistence.room:compiler:${LibraryVersions.Libs.Android.room}"
            )

            val dagger = AndroidLibrary(
                name = "com.google.dagger:dagger:${LibraryVersions.Libs.Android.dagger}"
            )
            val daggerCompiler = KaptLibrary(
                name = "com.google.dagger:dagger-compiler:${LibraryVersions.Libs.Android.dagger}"
            )

            val navigation = AndroidLibrary(
                name = "androidx.navigation:navigation-fragment-ktx:${LibraryVersions.Libs.Android.navigation}"
            )
            val navigationUi = AndroidLibrary(
                name = "androidx.navigation:navigation-ui-ktx:${LibraryVersions.Libs.Android.navigation}"
            )
            val keyboard = AndroidLibrary(
                name = "net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:${LibraryVersions.Libs.Android.keyboard}"
            )
            val worker = AndroidLibrary(
                name = "androidx.work:work-runtime-ktx:${LibraryVersions.Libs.Android.worker}"
            )
            val gson = AndroidLibrary(
                name = "com.google.code.gson:gson:${LibraryVersions.Libs.Android.gson}"
            )

        }

        object MultiPlatform {
            val kotlinStdLib = MultiPlatformLibrary(
                android = Android.kotlinStdLib.name,
                common = "org.jetbrains.kotlin:kotlin-stdlib-common:${LibraryVersions.Libs.MultiPlatform.kotlinStdLib}"
            )
            val coroutines = MultiPlatformLibrary(
                android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${LibraryVersions.Libs.MultiPlatform.coroutines}",
                common = "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${LibraryVersions.Libs.MultiPlatform.coroutines}",
                ios = "org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${LibraryVersions.Libs.MultiPlatform.coroutines}"
            )
            val serialization = MultiPlatformLibrary(
                android = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${LibraryVersions.Libs.MultiPlatform.serialization}",
                common = "org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${LibraryVersions.Libs.MultiPlatform.serialization}",
                ios = "org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:${LibraryVersions.Libs.MultiPlatform.serialization}"
            )

            val mokoMvvm = MultiPlatformLibrary(
                common = "dev.icerock.moko:mvvm:${LibraryVersions.Libs.MultiPlatform.mokoMvvm}",
                iosX64 = "dev.icerock.moko:mvvm-iosx64:${LibraryVersions.Libs.MultiPlatform.mokoMvvm}",
                iosArm64 = "dev.icerock.moko:mvvm-iosarm64:${LibraryVersions.Libs.MultiPlatform.mokoMvvm}"
            )
            val mokoResources = MultiPlatformLibrary(
                common = "dev.icerock.moko:resources:${LibraryVersions.Libs.MultiPlatform.mokoResources}",
                iosX64 = "dev.icerock.moko:resources-iosx64:${LibraryVersions.Libs.MultiPlatform.mokoResources}",
                iosArm64 = "dev.icerock.moko:resources-iosarm64:${LibraryVersions.Libs.MultiPlatform.mokoResources}"
            )
            val kodein = MultiPlatformLibrary(
                    common = "org.kodein.di:kodein-di-core:${LibraryVersions.Libs.MultiPlatform.kodein}"
            )
            val kodeinErased = MultiPlatformLibrary(
                    common = "org.kodein.di:kodein-di-erased:${LibraryVersions.Libs.MultiPlatform.kodein}"
            )

            val sqlDelight = MultiPlatformLibrary(
                    common = "com.squareup.sqldelight:runtime:${LibraryVersions.Libs.MultiPlatform.sqlDelight}",
                    android = "com.squareup.sqldelight:android-driver:${LibraryVersions.Libs.MultiPlatform.sqlDelight}"
            )
        }
    }

    val plugins: Map<String, String> = mapOf(
        "kotlin-android-extensions" to Plugins.androidExtensions,
        "kotlinx-serialization" to Plugins.kotlinSerialization,
        "dev.icerock.mobile.multiplatform-resources" to Plugins.mokoResources
    )
}