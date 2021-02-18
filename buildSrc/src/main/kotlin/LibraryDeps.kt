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
        val coroutines ="org.jetbrains.kotlinx:kotlinx-coroutines-android:${LibraryVersions.Libs.coroutines}"
        val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${LibraryVersions.Common.serialization}"
        val appCompat = "androidx.appcompat:appcompat:${LibraryVersions.Libs.appCompat}"
        val activityKtx = "androidx.activity:activity-ktx:${LibraryVersions.Libs.activity}"
        val material = "com.google.android.material:material:${LibraryVersions.Libs.material}"
        val fragment = "androidx.fragment:fragment:${LibraryVersions.Libs.fragment}"
        val recyclerView = "androidx.recyclerview:recyclerview:${LibraryVersions.Libs.recyclerView}"
        val constraintLayout = "androidx.constraintlayout:constraintlayout:${LibraryVersions.Libs.constraintLayout}"
        val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${LibraryVersions.Libs.lifecycle}"
        val lifecycleViewModelSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${LibraryVersions.Libs.lifecycle}"
        val lifecycleLiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${LibraryVersions.Libs.lifecycle}"
        val cardView = "androidx.cardview:cardview:${LibraryVersions.Libs.cardView}"
        val annotation = "androidx.annotation:annotation:${LibraryVersions.Libs.annotation}"
        val rxjava2 = "io.reactivex.rxjava2:rxjava:${LibraryVersions.Libs.rxjava2}"
        val paging = "androidx.paging:paging-runtime:${LibraryVersions.Libs.paging}"
        val reflect = "org.jetbrains.kotlin:kotlin-reflect:${LibraryVersions.kotlin}"
        val playCore = "com.google.android.play:core:${LibraryVersions.Libs.playCore}"
        val billing = "com.android.billingclient:billing-ktx:${LibraryVersions.Libs.billing}"
        val publisher = "com.google.apis:google-api-services-androidpublisher:${LibraryVersions.Libs.publisher}"
        val firebaseFirestore = "com.google.firebase:firebase-firestore-ktx:${LibraryVersions.Libs.firebaseFirestore}"
        val oauth2 = "com.google.auth:google-auth-library-oauth2-http:${LibraryVersions.Libs.auth}"
        val room = "androidx.room:room-runtime:${LibraryVersions.Libs.room}"
        val roomCompiler = "androidx.room:room-compiler:${LibraryVersions.Libs.room}"
        val roomKtx = "androidx.room:room-ktx:${LibraryVersions.Libs.room}"
        val dagger = "com.google.dagger:dagger:${LibraryVersions.Libs.dagger}"
        val daggerCompiler = "com.google.dagger:dagger-compiler:${LibraryVersions.Libs.dagger}"
        val navigation = "androidx.navigation:navigation-fragment-ktx:${LibraryVersions.Libs.navigation}"
        val navigationUi = "androidx.navigation:navigation-ui-ktx:${LibraryVersions.Libs.navigation}"
        val keyboard = "net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:${LibraryVersions.Libs.keyboard}"
        val worker = "androidx.work:work-runtime-ktx:${LibraryVersions.Libs.worker}"
        val gson = "com.google.code.gson:gson:${LibraryVersions.Libs.gson}"
        val coil = "io.coil-kt:coil:${LibraryVersions.Libs.coil}"
        val filemanager = "com.github.Merseyside:android-filemanager:${LibraryVersions.Libs.filemanager}"
        val typedDatastore = "androidx.datastore:datastore:${LibraryVersions.Libs.typedDataStore}"
    }
}