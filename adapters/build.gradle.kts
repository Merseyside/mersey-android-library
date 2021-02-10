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
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val androidLibs = listOf(
    LibraryDeps.Libs.Android.appCompat,
    LibraryDeps.Libs.Android.material,
    LibraryDeps.Libs.Android.recyclerView,
    LibraryDeps.Libs.Android.coroutines
)

dependencies {
    androidLibs.forEach { lib -> androidImplementation(lib) }
    api(LibraryDeps.Libs.Android.paging.name)

    implementation(project(LibraryModules.Android.utils))
}

repositories {
    mavenCentral()
}