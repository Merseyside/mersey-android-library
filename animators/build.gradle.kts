@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    with(catalogPlugins.plugins) {
        plugin(android.library)
        plugin(kotlin.android)
        id(mersey.android.convention.id())
        id(mersey.kotlin.convention.id())
        plugin(kotlin.kapt)
    }
    `android-maven-publish-config`
}

android {
    namespace = "com.merseyside.animators"
    compileSdk = Application.compileSdk

    defaultConfig {
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk
    }

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
}

kotlinConvention {
    setCompilerArgs(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xcontext-receivers"
    )
}

dependencies {
    implementation(androidLibs.appCompat)
    implementation(projects.utils)
    api(common.mersey.time)
}