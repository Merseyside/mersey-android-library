import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import extensions.androidImplementation

plugins {
    plugin(LibraryDeps.Plugins.androidLibrary)
    plugin(LibraryDeps.Plugins.kotlinAndroid)
    plugin(LibraryDeps.Plugins.kotlinKapt)
    plugin(LibraryDeps.Plugins.kotlinSerialization)
    plugin(LibraryDeps.Plugins.mavenPublish)
}

group = LibraryVersions.Application.publishingId
version = LibraryVersions.Application.version

android {
    compileSdkVersion(LibraryVersions.Android.compileSdk)

    defaultConfig {
        minSdkVersion(LibraryVersions.Android.minSdk)
        targetSdkVersion(LibraryVersions.Android.targetSdk)
        versionCode = LibraryVersions.Application.versionCode
        versionName = LibraryVersions.Application.version
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
    LibraryDeps.Libs.Android.coroutines,
    LibraryDeps.Libs.Android.appCompat,
    LibraryDeps.Libs.Android.material,
    LibraryDeps.Libs.Android.navigation,
    LibraryDeps.Libs.Android.rxjava2,
    LibraryDeps.Libs.Android.navigationUi,
    LibraryDeps.Libs.Android.lifecycle,
    LibraryDeps.Libs.Android.dagger,
    LibraryDeps.Libs.Android.worker,
    LibraryDeps.Libs.Android.gson,
    LibraryDeps.Libs.Android.keyboard,
    LibraryDeps.Libs.Android.room,
    LibraryDeps.Libs.Android.serialization
)

val modulez = listOf(
    LibraryModules.Android.utils,
    LibraryModules.Android.adapters,
    LibraryModules.Android.animators
)

dependencies {
    modulez.forEach { module -> implementation(project(module)) }
    androidLibs.forEach { lib -> androidImplementation(lib) }

    kaptLibrary(LibraryDeps.Libs.Android.daggerCompiler)
    kaptLibrary(LibraryDeps.Libs.Android.roomCompiler)
}

repositories {
    mavenCentral()
}