import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    plugin(LibraryDeps.Plugins.androidLibrary)
    plugin(LibraryDeps.Plugins.kotlinAndroid)
    plugin(LibraryDeps.Plugins.kotlinKapt)
    plugin(LibraryDeps.Plugins.kotlinSerialization)
    plugin(LibraryDeps.Plugins.jitpack)
}

group = "com.github.Merseyside"
version = LibraryVersions.Android.version

android {
    compileSdkVersion(LibraryVersions.Android.compileSdk)

    defaultConfig {
        minSdkVersion(LibraryVersions.Android.minSdk)
        targetSdkVersion(LibraryVersions.Android.targetSdk)
        versionCode = LibraryVersions.Android.versionCode
        versionName = LibraryVersions.Android.version
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("release") {
            isMinifyEnabled = false
            consumerProguardFiles("proguard-rules.pro")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures.dataBinding = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val androidLibs = listOf(
    LibraryDeps.Libs.Android.coroutines.name,
    LibraryDeps.Libs.Android.appCompat.name,
    LibraryDeps.Libs.Android.material.name,
    LibraryDeps.Libs.Android.navigation.name,
    LibraryDeps.Libs.Android.rxjava2.name,
    LibraryDeps.Libs.Android.navigationUi.name,
    LibraryDeps.Libs.Android.lifecycle.name,
    LibraryDeps.Libs.Android.dagger.name,
    LibraryDeps.Libs.Android.worker.name,
    LibraryDeps.Libs.Android.gson.name,
    LibraryDeps.Libs.Android.keyboard.name,
    LibraryDeps.Libs.Android.room.name,
    LibraryDeps.Libs.MultiPlatform.serialization.android!!
)

val modulez = listOf(
    LibraryModules.Android.utils,
    LibraryModules.Android.adapters,
    LibraryModules.Android.animators
)

dependencies {
    modulez.forEach { module -> implementation(project(module)) }
    androidLibs.forEach { lib -> implementation(lib)}

    kaptLibrary(LibraryDeps.Libs.Android.daggerCompiler)
    kaptLibrary(LibraryDeps.Libs.Android.roomCompiler)
}

repositories {
    mavenCentral()
}