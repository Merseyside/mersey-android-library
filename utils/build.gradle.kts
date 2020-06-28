plugins {
    plugin(LibraryDeps.Plugins.androidLibrary)
    plugin(LibraryDeps.Plugins.kotlinAndroid)
    plugin(LibraryDeps.Plugins.kotlinAndroidExtensions)
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
    LibraryDeps.Libs.Android.kotlinStdLib.name,
    LibraryDeps.Libs.Android.appCompat.name,
    LibraryDeps.Libs.Android.material.name,
    LibraryDeps.Libs.Android.coroutines.name,
    LibraryDeps.Libs.Android.reflect.name,
    LibraryDeps.Libs.Android.paging.name,
    LibraryDeps.Libs.Android.billing.name,
    LibraryDeps.Libs.Android.billingKtx.name,
    LibraryDeps.Libs.Android.publisher.name,
    LibraryDeps.Libs.Android.oauth2.name,
    LibraryDeps.Libs.MultiPlatform.serialization.android!!,
    LibraryDeps.Libs.Android.firebaseFirestore.name,
    LibraryDeps.Libs.Android.playCore.name
)

dependencies {

    androidLibs.forEach { lib -> implementation(lib)}
}

//apply("../common-gradle.gradle")
repositories {
    mavenCentral()
}