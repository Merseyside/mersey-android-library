plugins {
    plugin(LibraryDeps.Plugins.kotlinMultiplatform)
    plugin(LibraryDeps.Plugins.androidLibrary)
    plugin(LibraryDeps.Plugins.kotlinAndroidExtensions)
    plugin(LibraryDeps.Plugins.kotlinKapt)
    plugin(LibraryDeps.Plugins.mobileMultiplatform)
    plugin(LibraryDeps.Plugins.mavenPublish)
    plugin(LibraryDeps.Plugins.resources)
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

multiplatformResources {
    multiplatformResourcesPackage = LibraryVersions.Application.packageName // required
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
    LibraryModules.Android.archy,
    LibraryModules.Android.utils
)

dependencies {
    mppLibs.forEach { mppLibrary(it) }
    androidLibraries.forEach { lib -> androidLibrary(lib) }
    merseyModules.forEach { module -> implementation(project(module)) }

    mppModule(LibraryModules.MultiPlatform.utils)

    kaptLibrary(LibraryDeps.Libs.Android.daggerCompiler)
    compileOnly("javax.annotation:jsr250-api:1.0")
}

publishing {
    repositories.maven("https://api.bintray.com/maven/merseysoft/mersey-library/kmp-clean-mvvm-arch/;publish=1") {
        name = "bintray"

        credentials {
            username = System.getProperty("BINTRAY_USERNAME")
            password = System.getProperty("BINTRAY_KEY")
        }
    }
}

repositories {
    mavenCentral()
}