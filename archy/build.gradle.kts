import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    plugin(LibraryDeps.Plugins.androidLibrary)
    plugin(LibraryDeps.Plugins.kotlinAndroid)
    plugin(LibraryDeps.Plugins.kotlinKapt)
    plugin(LibraryDeps.Plugins.kotlinSerialization)
    plugin(LibraryDeps.Plugins.mavenPublish)
}

group = LibraryVersions.Application.groupId
version = LibraryVersions.Application.version

android {
    compileSdkVersion(LibraryVersions.Application.compileSdk)

    defaultConfig {
        minSdkVersion(LibraryVersions.Application.minSdk)
        targetSdkVersion(LibraryVersions.Application.targetSdk)
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
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

val androidLibs = listOf(
    LibraryDeps.Libs.coroutines,
    LibraryDeps.Libs.appCompat,
    LibraryDeps.Libs.material,
    LibraryDeps.Libs.navigation,
    LibraryDeps.Libs.rxjava2,
    LibraryDeps.Libs.navigationUi,
    LibraryDeps.Libs.dagger,
    LibraryDeps.Libs.worker,
    LibraryDeps.Libs.gson,
    LibraryDeps.Libs.keyboard,
    LibraryDeps.Libs.room,
    LibraryDeps.Libs.serialization,
    LibraryDeps.Libs.MerseyLibs.time
)

val modulez = listOf(
    LibraryModules.utils,
    LibraryModules.adapters,
    LibraryModules.animators
)

dependencies {
    modulez.forEach { module -> implementation(project(module)) }
    androidLibs.forEach { lib -> implementation(lib) }

    kapt(LibraryDeps.Libs.daggerCompiler)
    kapt(LibraryDeps.Libs.roomCompiler)
}

afterEvaluate {
    publishing.publications {
        create<MavenPublication>("release") {
            groupId = group.toString()
            artifactId = project.name
            version = rootProject.version.toString()
            from(components["release"])
        }
    }

    repositories {
        mavenCentral()
    }
}