plugins {
    plugin(LibraryDeps.Plugins.kotlinMultiplatform)
    plugin(LibraryDeps.Plugins.androidLibrary)
    plugin(LibraryDeps.Plugins.kotlinAndroidExtensions)
    plugin(LibraryDeps.Plugins.kotlinKapt)
    plugin(LibraryDeps.Plugins.mobileMultiplatform)
    plugin(LibraryDeps.Plugins.jitpack)
    plugin(LibraryDeps.Plugins.mavenPublish)
}

group = LibraryVersions.Application.applicationId
version = LibraryVersions.Android.version

android {
    compileSdkVersion(LibraryVersions.Android.compileSdk)

    defaultConfig {
        minSdkVersion(LibraryVersions.Android.minSdk)
        targetSdkVersion(LibraryVersions.Android.targetSdk)
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/*.kotlin_module")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {
    android {
        publishAllLibraryVariants()
        publishLibraryVariantsGroupedByFlavor = true
        //publishLibraryVariants = listOf("release")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val mppLibs = listOf(
    LibraryDeps.Libs.MultiPlatform.kotlinStdLib,
    LibraryDeps.Libs.MultiPlatform.serialization,
    LibraryDeps.Libs.MultiPlatform.kodein,
    LibraryDeps.Libs.MultiPlatform.kodeinErased,
    LibraryDeps.Libs.MultiPlatform.mokoResources,
    LibraryDeps.Libs.MultiPlatform.ktorClient
)

val androidLibraries = listOf(
    LibraryDeps.Libs.Android.appCompat,
    LibraryDeps.Libs.Android.publisher,
    LibraryDeps.Libs.Android.oauth2,
    LibraryDeps.Libs.Android.billing,
    LibraryDeps.Libs.Android.billingKtx
)

val merseyModules = listOf(
    LibraryModules.Android.utils
)

dependencies {
    mppLibs.forEach { mppLibrary(it) }
    androidLibraries.forEach { lib -> androidLibrary(lib) }
    merseyModules.forEach { module -> implementation(project(module)) }

    kaptLibrary(LibraryDeps.Libs.Android.daggerCompiler)
}

publishing {
    repositories.maven("https://api.bintray.com/maven/merseysoft/mersey-library/kmp-utils/;publish=1") {
        name = "bintray"

        credentials {
            username = System.getProperty("BINTRAY_USERNAME")
            password = System.getProperty("BINTRAY_KEY")
        }
    }
}
apply("common-gradle.gradle")
repositories {
    mavenCentral()
}