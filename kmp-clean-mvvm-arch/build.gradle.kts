plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("kotlin-android-extensions")
    kotlin("kapt")
    id("dev.icerock.mobile.multiplatform")
    id("dev.icerock.mobile.multiplatform-resources")
    id("maven-publish")
}

group = "com.merseyside.merseyLib"
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

    flavorDimensions("nav")
    productFlavors {
        create("navigation") {
            dimension = "nav"
            matchingFallbacks = listOf("navigation")
        }

        create("standart") {
            dimension = "nav"
            matchingFallbacks = listOf("standart")
        }
    }
}

kotlin {
    android {
        publishLibraryVariantsGroupedByFlavor = true
        publishLibraryVariants = listOf("navigationRelease", "standartRelease")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val mppLibs = listOf(
    LibraryDeps.Libs.MultiPlatform.kotlinStdLib,
    LibraryDeps.Libs.MultiPlatform.coroutines,
    LibraryDeps.Libs.MultiPlatform.serialization,
    LibraryDeps.Libs.MultiPlatform.mokoMvvm,
    LibraryDeps.Libs.MultiPlatform.kodein,
    LibraryDeps.Libs.MultiPlatform.kodeinErased,
    LibraryDeps.Libs.MultiPlatform.sqlDelight
)

val androidLibraries = listOf(
    LibraryDeps.Libs.Android.appCompat,
    LibraryDeps.Libs.Android.lifecycle,
    LibraryDeps.Libs.Android.dagger,
    LibraryDeps.Libs.Android.annotation
)

val merseyModules = listOf(
    LibraryModules.Android.cleanMvvmArch,
    LibraryModules.Android.utils
)

setupFramework(
    exports = mppLibs
)

dependencies {
    mppLibs.forEach { mppLibrary(it) }
    androidLibraries.forEach { lib -> androidLibrary(lib) }
    merseyModules.forEach { module -> implementation(project(module)) }

    mppModule(LibraryModules.MultiPlatform.utils)

    kaptLibrary(LibraryDeps.Libs.Android.daggerCompiler)
    compileOnly("javax.annotation:jsr250-api:1.0")
}

multiplatformResources {
    multiplatformResourcesPackage = "com.merseyside.merseyLib"
}


publishing {
    repositories.maven("https://api.bintray.com/maven/merseysoftware/mersey-library/kmp-clean-mvvm-arch/;publish=1") {
        name = "bintray"

        credentials {
            username = System.getProperty("BINTRAY_USERNAME")
            password = System.getProperty("BINTRAY_KEY")
        }
    }
}
