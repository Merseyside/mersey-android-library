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

        val kotlinSerialization = GradlePlugin(
            id = "kotlinx-serialization",
            module = "org.jetbrains.kotlin:kotlin-serialization:${LibraryVersions.Plugins.serialization}"
        )
    }

    object Libs {
        const val coroutines ="org.jetbrains.kotlinx:kotlinx-coroutines-android:${LibraryVersions.Libs.coroutines}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${LibraryVersions.Common.serialization}"
        const val appCompat = "androidx.appcompat:appcompat:${LibraryVersions.Libs.appCompat}"
        const val activityKtx = "androidx.activity:activity-ktx:${LibraryVersions.Libs.activity}"
        const val material = "com.google.android.material:material:${LibraryVersions.Libs.material}"
        const val fragment = "androidx.fragment:fragment:${LibraryVersions.Libs.fragment}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${LibraryVersions.Libs.recyclerView}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${LibraryVersions.Libs.constraintLayout}"
        const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${LibraryVersions.Libs.lifecycle}"
        const val lifecycleViewModelSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${LibraryVersions.Libs.lifecycle}"
        const val lifecycleLiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${LibraryVersions.Libs.lifecycle}"
        const val cardView = "androidx.cardview:cardview:${LibraryVersions.Libs.cardView}"
        const val annotation = "androidx.annotation:annotation:${LibraryVersions.Libs.annotation}"
        const val rxjava2 = "io.reactivex.rxjava2:rxjava:${LibraryVersions.Libs.rxjava2}"
        const val paging = "androidx.paging:paging-runtime:${LibraryVersions.Libs.paging}"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${LibraryVersions.kotlin}"
        const val playCore = "com.google.android.play:core:${LibraryVersions.Libs.playCore}"
        const val billing = "com.android.billingclient:billing-ktx:${LibraryVersions.Libs.billing}"
        const val publisher = "com.google.apis:google-api-services-androidpublisher:${LibraryVersions.Libs.publisher}"
        const val firebaseFirestore = "com.google.firebase:firebase-firestore-ktx:${LibraryVersions.Libs.firebaseFirestore}"
        const val oauth2 = "com.google.auth:google-auth-library-oauth2-http:${LibraryVersions.Libs.auth}"
        const val room = "androidx.room:room-runtime:${LibraryVersions.Libs.room}"
        const val roomCompiler = "androidx.room:room-compiler:${LibraryVersions.Libs.room}"
        const val roomKtx = "androidx.room:room-ktx:${LibraryVersions.Libs.room}"
        const val dagger = "com.google.dagger:dagger:${LibraryVersions.Libs.dagger}"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:${LibraryVersions.Libs.dagger}"
        const val navigation = "androidx.navigation:navigation-fragment-ktx:${LibraryVersions.Libs.navigation}"
        const val navigationUi = "androidx.navigation:navigation-ui-ktx:${LibraryVersions.Libs.navigation}"
        const val keyboard = "net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:${LibraryVersions.Libs.keyboard}"
        const val worker = "androidx.work:work-runtime-ktx:${LibraryVersions.Libs.worker}"
        const val gson = "com.google.code.gson:gson:${LibraryVersions.Libs.gson}"
        const val coil = "io.coil-kt:coil:${LibraryVersions.Libs.coil}"
        const val filemanager = "com.github.Merseyside:android-filemanager:${LibraryVersions.Libs.filemanager}"
        const val typedDatastore = "androidx.datastore:datastore:${LibraryVersions.Libs.typedDataStore}"
        const val location = "com.google.android.gms:play-services-location:${LibraryVersions.Libs.location}"

        object MerseyLibs {
            const val time = "io.github.merseyside:time:${LibraryVersions.Libs.time}"
        }
    }


}