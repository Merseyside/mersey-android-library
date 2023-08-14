plugins {
    with(catalogPlugins.plugins) {
        plugin(android.library)
        plugin(kotlin.android)
        plugin(kotlin.serialization)
        id(mersey.android.extension.id())
        id(mersey.kotlin.extension.id())
        plugin(kotlin.kapt)
    }
    `maven-publish-plugin`
}

android {
    namespace = "com.merseyside.utils"
    compileSdk = Application.compileSdk

    defaultConfig {
        minSdk = Application.minSdk
    }

    buildFeatures {
        dataBinding = true
    }

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
    androidLibs.navigation,
    androidLibs.appCompat,
    androidLibs.androidx.core,
    androidLibs.constraintLayout,
    androidLibs.material,
    androidLibs.paging,
    androidLibs.billing,
    androidLibs.publisher,
    androidLibs.oauth2,
    androidLibs.playCore,
    androidLibs.coil,
    androidLibs.location
)

dependencies {

//    implementation("org.apache.httpcomponents:httpclient")
//    constraints {
//        implementation("org.apache.httpcomponents:httpclient:4.5.14") {
//            because("previous versions have a bug impacting this application")
//        }
//    }
//
//    implementation("org.apache.httpcomponents:httpcore")
//    constraints {
//        implementation("org.apache.httpcomponents:httpcore:4.4.16") {
//            because("previous versions have a bug impacting this application")
//        }
//    }

//    implementation(androidLibs.publisher) {
//        exclude("org.apache.httpcomponents", "httpclient")
//        exclude("org.apache.httpcomponents", "httpcore")
//    }

    api(common.bundles.mersey.time)
    api(common.mersey.kotlin.ext)

    androidLibz.forEach { lib -> implementation(lib) }
}