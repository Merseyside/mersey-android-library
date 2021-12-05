plugins {
    id(Plugins.androidApplication)
    id(Plugins.kotlinConvention)
    id(Plugins.kotlinKapt)
    id(Plugins.kotlinSerialization)
}

android {
    compileSdkVersion(Application.compileSdk)

    defaultConfig {
        minSdkVersion(Application.minSdk)
        targetSdkVersion(Application.targetSdk)
        versionCode = Application.versionCode
        versionName = Application.version

        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true

        lint {
            isAbortOnError = false
        }
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

    buildFeatures.dataBinding = true

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
    compileOnly("javax.annotation:jsr250-api:1.0")
}