plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinKapt)
    id(Plugins.kotlinSerialization)
    id(Plugins.mavenPublish)
}

group = Application.groupId
version = Application.version

android {
    compileSdkVersion(Application.compileSdk)

    defaultConfig {
        minSdkVersion(Application.minSdk)
        targetSdkVersion(Application.targetSdk)
        versionCode = Application.versionCode
        versionName = Application.version
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

    buildFeatures.dataBinding = true
}

val android = listOf(
    androidLibs.coroutines,
    common.serialization,
    androidLibs.appCompat,
    androidLibs.material,
    androidLibs.rxjava2,
    androidLibs.dagger,
    androidLibs.worker,
    androidLibs.gson,
    androidLibs.keyboard,
    androidLibs.room
)

val androidBundles = listOf(
    androidLibs.bundles.lifecycle,
    androidLibs.bundles.navigation
)

val modulez = listOf(
    projects.utils,
    projects.adapters,
    projects.animators
)

dependencies {
    api(common.merseyLib.time)
    modulez.forEach { module -> implementation(module) }
    android.forEach { lib -> implementation(lib) }
    androidBundles.forEach { bundle -> implementation(bundle) }

    kapt(androidLibs.daggerCompiler)
    kapt(androidLibs.roomCompiler)
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