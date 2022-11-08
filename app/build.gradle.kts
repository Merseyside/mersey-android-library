@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    with(catalogPlugins.plugins) {
        plugin(android.application)
        plugin(kotlin.android)
        id(mersey.android.convention.id())
        id(mersey.kotlin.convention.id())
        plugin(kotlin.serialization)
        plugin(kotlin.kapt)
    }
}

android {
    namespace = "com.merseyside.merseyLib"
    compileSdk = Application.compileSdk

    defaultConfig {
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk
        versionCode = 1
        versionName = "0.1.0"

        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
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

kotlinConvention {
    setCompilerArgs(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xcontext-receivers",
        "-Xskip-prerelease-check"
    )
}

val androidLibz = listOf(
    androidLibs.coroutines,
    androidLibs.recyclerView,
    androidLibs.navigation,
    androidLibs.navigationUi,
    androidLibs.constraintLayout,
    androidLibs.lifecycleLiveDataKtx,
    androidLibs.appCompat,
    androidLibs.material,
    androidLibs.cardView,
    androidLibs.dagger
)

val modulez = listOf(
    projects.utils,
    projects.archy,
    projects.adapters,
    projects.animators
)

dependencies {
    implementation(common.serialization)
    modulez.forEach { module -> implementation(module) }
    androidLibz.forEach { lib -> implementation(lib) }

    implementation(androidLibs.mersey.filemanager) {
        exclude(group = "com.github.Merseyside.mersey-android-library", module = "utils")
    }

    kapt(androidLibs.dagger.compiler)
}