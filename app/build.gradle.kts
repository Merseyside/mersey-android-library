plugins {
    plugin(LibraryDeps.Plugins.androidApplication)
    plugin(LibraryDeps.Plugins.kotlinAndroid)
    plugin(LibraryDeps.Plugins.kotlinKapt)
}

android {
    compileSdkVersion(LibraryVersions.Android.compileSdk)

    defaultConfig {
        minSdkVersion(LibraryVersions.Android.minSdk)
        targetSdkVersion(LibraryVersions.Android.targetSdk)
        versionCode = LibraryVersions.Android.versionCode
        versionName = LibraryVersions.Android.version

        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("release") {
            isMinifyEnabled = false
            consumerProguardFiles("proguard-rules.pro")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/*.kotlin_module")
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

    buildFeatures {
        dataBinding = true
    }

    sourceSets.getByName("main") {
        res.srcDir("src/main/res/")
        res.srcDir("src/main/res/layouts/fragment")
        res.srcDir("src/main/res/layouts/activity")
        res.srcDir("src/main/res/layouts/dialog")
        res.srcDir("src/main/res/layouts/view")
        res.srcDir("src/main/res/value/values-light")
        res.srcDir("src/main/res/value/values-night")
    }

}

val androidLibs = listOf(
    LibraryDeps.Libs.Android.recyclerView.name,
    "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2",
    LibraryDeps.Libs.Android.navigation.name,
    LibraryDeps.Libs.Android.navigationUi.name,
    LibraryDeps.Libs.Android.constraintLayout.name,
    LibraryDeps.Libs.Android.lifecycleLiveDataKtx.name,
    LibraryDeps.Libs.Android.appCompat.name,
    LibraryDeps.Libs.Android.material.name,
    LibraryDeps.Libs.Android.lifecycle.name,
    LibraryDeps.Libs.Android.cardView.name,
    LibraryDeps.Libs.Android.annotation.name,
    LibraryDeps.Libs.Android.dagger.name
)

val modulez = listOf(
    LibraryModules.Android.utils,
    LibraryModules.Android.archy,
    LibraryModules.Android.adapters,
    LibraryModules.Android.animators
)

dependencies {
    modulez.forEach { module -> implementation(project(module)) }
    androidLibs.forEach { lib -> implementation(lib) }

    kaptLibrary(LibraryDeps.Libs.Android.daggerCompiler)
    compileOnly("javax.annotation:jsr250-api:1.0")
}