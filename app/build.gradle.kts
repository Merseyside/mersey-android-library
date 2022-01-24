plugins {
    id(Plugins.androidApplication)
    id(Plugins.kotlinConvention)
    id(Plugins.kotlinKapt)
    id(Plugins.kotlinSerialization)
}

android {
    compileSdk = Application.compileSdk

    defaultConfig {
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk
        versionCode = Metadata.versionCode
        versionName = Metadata.version

        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true

        lint {
            isAbortOnError = false
        }
    }

    buildFeatures.dataBinding = true

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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/*.kotlin_module"
            )
        )
    }

    sourceSets.getByName("main") {
        res.srcDirs(
            "src/main/res/",
            "src/main/res/layouts/fragment",
            "src/main/res/layouts/activity",
            "src/main/res/layouts/dialog",
            "src/main/res/layouts/view",
            "src/main/res/value/values-light",
            "src/main/res/value/values-night"
        )
    }
}

val android = listOf(
    common.serialization,
    androidLibs.coroutines,
    androidLibs.recyclerView,
    androidLibs.navigation,
    androidLibs.navigationUi,
    androidLibs.constraintLayout,
    androidLibs.lifecycleLiveDataKtx,
    androidLibs.appCompat,
    androidLibs.material,
    androidLibs.cardView,
    androidLibs.annotation,
    androidLibs.dagger
)

val modulez = listOf(
    projects.utils,
    projects.archy,
    projects.adapters,
    projects.animators
)

dependencies {
    modulez.forEach { module -> implementation(module) }
    android.forEach { lib -> implementation(lib) }

    implementation(androidLibs.merseyLib.filemanager) {
        exclude(group = "com.github.Merseyside.mersey-android-library", module = "utils")
    }

    kapt(androidLibs.daggerCompiler)
}