object LibraryDeps {
    object Plugins {
        val androidApplication = GradlePlugin(id = "com.android.application")
        val kotlinKapt = GradlePlugin(id = "kotlin-kapt")
        val kotlinAndroid = GradlePlugin(id = "kotlin-android")
        val mavenPublish = GradlePlugin(id = "maven-publish")

        val androidLibrary = GradlePlugin(
            id = "com.android.library",
            module = "com.android.tools.build:gradle:${LibraryVersions.Plugins.gradle}"
        )

        val kotlinMultiplatform = GradlePlugin(
            id = "org.jetbrains.kotlin.multiplatform",
            module = "org.jetbrains.kotlin:kotlin-gradle-plugin:${LibraryVersions.Plugins.kotlin}"
        )

        val kotlinSerialization = GradlePlugin(
            id = "kotlinx-serialization",
            module = "org.jetbrains.kotlin:kotlin-serialization:${LibraryVersions.Plugins.serialization}"
        )

        val resources = GradlePlugin(
            id = "dev.icerock.mobile.multiplatform-resources",
            module = "dev.icerock.moko:resources-generator:${LibraryVersions.Plugins.mokoResources}"
        )

        val sqldelight = GradlePlugin(
            id = "com.squareup.sqldelight",
            module = "com.squareup.sqldelight:gradle-plugin:${LibraryVersions.Plugins.sqlDelight}"
        )

        val kotlinParcelize = GradlePlugin(
            id = "kotlin-parcelize"
        )
    }

    object Libs {
        object Android {
            val coroutines = AndroidLibrary(
                name = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${LibraryVersions.Libs.Android.coroutines}"
            )
            val serialization = AndroidLibrary(
                name = "org.jetbrains.kotlinx:kotlinx-serialization-json:${LibraryVersions.Common.serialization}"
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
                name = "androidx.lifecycle:lifecycle-extensions:${LibraryVersions.Libs.Android.lifecycle_extension}"
            )
            val lifecycleViewModel = AndroidLibrary(
                name = "androidx.lifecycle:lifecycle-viewmodel-ktx:${LibraryVersions.Libs.Android.lifecycle}"
            )
            val lifecycleViewModelSavedState = AndroidLibrary(
                name = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${LibraryVersions.Libs.Android.lifecycle}"
            )
            val lifecycleLiveDataKtx = AndroidLibrary(
                name = "androidx.lifecycle:lifecycle-livedata-ktx:${LibraryVersions.Libs.Android.lifecycle}"
            )
            val cardView = AndroidLibrary(
                name = "androidx.cardview:cardview:${LibraryVersions.Libs.Android.cardView}"
            )
            val annotation = AndroidLibrary(
                name = "androidx.annotation:annotation:${LibraryVersions.Libs.Android.annotation}"
            )
            val rxjava2 = AndroidLibrary(
                name = "io.reactivex.rxjava2:rxjava:${LibraryVersions.Libs.Android.rxjava2}"
            )
            val paging = AndroidLibrary(
                name = "androidx.paging:paging-runtime:${LibraryVersions.Libs.Android.paging}"
            )
            val reflect = AndroidLibrary(
                name = "org.jetbrains.kotlin:kotlin-reflect:${LibraryVersions.kotlin}"
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
                name = "androidx.room:room-runtime:${LibraryVersions.Libs.Android.room}"
            )
            val roomCompiler = KaptLibrary(
                name = "androidx.room:room-compiler:${LibraryVersions.Libs.Android.room}"
            )
            val roomKtx = KaptLibrary(
                name = "androidx.room:room-ktx:${LibraryVersions.Libs.Android.room}"
            )
            val dagger = AndroidLibrary(
                name = "com.google.dagger:dagger:${LibraryVersions.Libs.Android.dagger}"
            )
            val daggerCompiler = KaptLibrary(
                name = "com.google.dagger:dagger-compiler:${LibraryVersions.Libs.Android.dagger}"
            )
            val koin = AndroidLibrary(
                name = "org.koin:koin-android:${LibraryVersions.Common.koin}"
            )
            val koinViewModels = AndroidLibrary(
                name = "org.koin:koin-androidx-viewmodel:${LibraryVersions.Common.koin}"
            )
            val koinExt = AndroidLibrary(
                name = "org.koin:koin-androidx-ext:${LibraryVersions.Common.koin}"
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
            val coil = AndroidLibrary(
                name = "io.coil-kt:coil:${LibraryVersions.Libs.Android.coil}"
            )
            val filemanager = AndroidLibrary(
                name = "com.github.Merseyside:android-filemanager:${LibraryVersions.Libs.Android.filemanager}"
            )
            val typedDatastore = AndroidLibrary(
                name = "androidx.datastore:datastore:${LibraryVersions.Libs.Android.typedDataStore}"
            )
            val mokoMvvmDatabinding = AndroidLibrary(
                name = "dev.icerock.moko:mvvm-databinding:${LibraryVersions.Libs.Android.mokoMvvm}"
            )
            val mokoMvvmViewbinding = AndroidLibrary(
                name = "dev.icerock.moko:mvvm-viewbinding:${LibraryVersions.Libs.Android.mokoMvvm}"
            )
        }

        object MultiPlatform {
            val kotlinStdLib = MultiPlatformLibrary(
                android = "org.jetbrains.kotlin:kotlin-stdlib:${LibraryVersions.Common.kotlinStdLib}",
                common = "org.jetbrains.kotlin:kotlin-stdlib-common:${LibraryVersions.Common.kotlinStdLib}"
            )
            val coroutines = MultiPlatformLibrary(
                android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${LibraryVersions.Common.coroutines}",
                common = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${LibraryVersions.Common.coroutines}",
                iosX64 = "org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${LibraryVersions.Common.coroutines}",
                iosArm64 = "org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${LibraryVersions.Common.coroutines}"
            )
            val serializationJson = MultiPlatformLibrary(
                common = "org.jetbrains.kotlinx:kotlinx-serialization-json:${LibraryVersions.Common.serialization}"
            )
            val serializationProtobuf = MultiPlatformLibrary(
                common = "org.jetbrains.kotlinx:kotlinx-serialization-protobuf:${LibraryVersions.Common.serialization}"
            )
            val ktorClient = MultiPlatformLibrary(
                android = "io.ktor:ktor-client-android:${LibraryVersions.Libs.MultiPlatform.ktor}",
                common = "io.ktor:ktor-client-core:${LibraryVersions.Libs.MultiPlatform.ktor}",
                iosX64 = "io.ktor:ktor-client-ios:${LibraryVersions.Libs.MultiPlatform.ktor}",
                iosArm64 = "io.ktor:ktor-client-ios:${LibraryVersions.Libs.MultiPlatform.ktor}"
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
            val koin = MultiPlatformLibrary(
                common = "org.koin:koin-core:${LibraryVersions.Common.koin}",
                android = "org.koin:koin-android:${LibraryVersions.Common.koin}"
            )
            val sqlDelight = MultiPlatformLibrary(
                common = "com.squareup.sqldelight:runtime:${LibraryVersions.Libs.MultiPlatform.sqlDelight}",
                android = "com.squareup.sqldelight:android-driver:${LibraryVersions.Libs.MultiPlatform.sqlDelight}"
            )
        }
    }
}