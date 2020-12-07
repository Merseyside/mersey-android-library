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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val androidLibs = listOf(
    LibraryDeps.Libs.Android.appCompat.name
)

dependencies {
    implementation(project(LibraryModules.Android.utils))

    androidLibs.forEach { lib -> implementation(lib)}
}

repositories {
    mavenCentral()
}