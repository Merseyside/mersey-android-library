plugins {
    id ("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("com.github.dcendents.android-maven")
}

apply("../common-gradle.gradle")

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

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    dataBinding {
        isEnabled = true
    }
}

val androidLibs = listOf(
    LibraryDeps.Libs.Android.kotlinStdLib.name,
    LibraryDeps.Libs.Android.appCompat.name
)

dependencies {

    implementation(project(":utils"))
    androidLibs.forEach { lib -> implementation(lib)}
}
