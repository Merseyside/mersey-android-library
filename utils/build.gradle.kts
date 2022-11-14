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
    namespace = "com.merseyside.utils"
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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
}

kotlinExtension {
    setCompilerArgs(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xcontext-receivers"
    )
}

val androidLibz = listOf(
    common.serialization,
    common.reflect,
    androidLibs.coroutines,
    androidLibs.appCompat,
    androidLibs.androidx.core,
    androidLibs.material,
    androidLibs.paging,
    androidLibs.billing,
    androidLibs.publisher,
    androidLibs.oauth2,
    androidLibs.playCore,
    androidLibs.coil,
    androidLibs.location,
    androidLibs.navigation
)

dependencies {

    api(common.bundles.mersey.time)
    api(common.mersey.kotlin.ext)

    androidLibz.forEach { lib -> implementation(lib) }
}