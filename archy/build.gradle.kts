import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    plugin(Plugins.androidLibrary)
    plugin(Plugins.kotlinAndroid)
    plugin(Plugins.kotlinKapt)
    plugin(Plugins.kotlinSerialization)
    plugin(Plugins.mavenPublish)
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
    libs.coroutines,
    libs.serialization,
    libs.appCompat,
    libs.material,
    libs.rxjava2,
    libs.dagger,
    libs.worker,
    libs.gson,
    libs.keyboard,
    libs.room
)

val androidBundles = listOf(
    libs.bundles.lifecycle,
    libs.bundles.navigation
)

val modulez = listOf(
    projects.utils,
    projects.adapters,
    projects.animators
)

dependencies {
    api(libs.merseyLib.time)
    modulez.forEach { module -> implementation(module) }
    androidLibs.forEach { lib -> implementation(lib) }
    androidBundles.forEach { bundle -> implementation(bundle) }

    kapt(libs.daggerCompiler)
    kapt(libs.roomCompiler)
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