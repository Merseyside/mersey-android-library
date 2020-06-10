plugins {
    id ("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("com.github.dcendents.android-maven")
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

    flavorDimensions("nav")
    defaultPublishConfig = "standartRelease"

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

    productFlavors {
        create("navigation") {
            dimension = "nav"
            matchingFallbacks = listOf("navigation")
        }

//        create("standart") {
//            dimension = "nav"
//            matchingFallbacks = listOf("standart")
//        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dataBinding {
        isEnabled = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val androidLibs = listOf(
    LibraryDeps.Libs.Android.kotlinStdLib.name,
    LibraryDeps.Libs.Android.coroutines.name,
    LibraryDeps.Libs.Android.coroutinesCore.name,
    LibraryDeps.Libs.Android.appCompat.name,
    LibraryDeps.Libs.Android.material.name,
    LibraryDeps.Libs.Android.navigation.name,
    LibraryDeps.Libs.Android.navigationUi.name,
    LibraryDeps.Libs.Android.lifecycle.name,
    LibraryDeps.Libs.Android.dagger.name,
    LibraryDeps.Libs.Android.worker.name,
    LibraryDeps.Libs.Android.gson.name,
    LibraryDeps.Libs.Android.keyboard.name,
    LibraryDeps.Libs.Android.reflect.name,
    LibraryDeps.Libs.Android.room.name,
    LibraryDeps.Libs.MultiPlatform.serialization.android!!
)

dependencies {

    implementation(project(":utils"))
    implementation(project(":adapters"))
    implementation(project(":animators"))

    androidLibs.forEach { lib -> implementation(lib)}

    kaptLibrary(LibraryDeps.Libs.Android.daggerCompiler)
    kaptLibrary(LibraryDeps.Libs.Android.roomCompiler)
    compileOnly("javax.annotation:jsr250-api:1.0")
}

apply("common-gradle.gradle")
repositories {
    mavenCentral()
}