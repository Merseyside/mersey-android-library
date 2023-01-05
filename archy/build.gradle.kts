@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    with(catalogPlugins.plugins) {
        plugin(android.library)
        plugin(kotlin.android)
        plugin(kotlin.serialization)
        id(mersey.android.extension.id())
        id(mersey.kotlin.extension.id())
        plugin(kotlin.kapt)
    }
    `android-maven-publish-config`
}

android {
    namespace = "com.merseyside.archy"
    compileSdk = Application.compileSdk

    defaultConfig {
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk
    }

    buildFeatures.dataBinding = true

    lint {
        lintConfig = rootProject.file(".lint/config.xml")
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

    val basePath = "src/main/res"
    sourceSets.getByName("main") {
        res.srcDir(basePath)
        res.srcDir("$basePath/layouts/valueSwitcher")
    }
}

kotlinExtension {
    setCompilerArgs(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xcontext-receivers"
    )
}

val androidLibz = listOf(
    androidLibs.coroutines,
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
    projects.animators
)

dependencies {
    implementation(common.serialization)
    api(common.bundles.mersey.time)

    modulez.forEach { module -> implementation(module) }
    
    androidLibz.forEach { lib -> implementation(lib) }
    androidBundles.forEach { bundle -> implementation(bundle) }

    kapt(androidLibs.dagger.compiler)
    kapt(androidLibs.room.compiler)
}