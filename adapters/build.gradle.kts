@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    with(catalogPlugins.plugins) {
        plugin(android.library)
        plugin(kotlin.android)
        plugin(kotlin.serialization)
        id(mersey.android.convention.id())
        id(mersey.kotlin.convention.id())
        plugin(kotlin.kapt)
    }
    `maven-publish-config`
}

android {
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
    listOf(
        androidLibs.appCompat,
        androidLibs.material,
        androidLibs.recyclerView,
        androidLibs.coroutines
    ).forEach { lib -> implementation(lib) }

    api(androidLibs.paging)
    api(common.merseyLib.time)

    implementation(projects.utils)
}

tasks {
    register<Jar>("withSourcesJar") {
        archiveClassifier.set("sources")
        from(android.sourceSets.getByName("main").java.srcDirs)
    }
}
