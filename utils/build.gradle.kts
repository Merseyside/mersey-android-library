plugins {
    id(Plugins.androidConvention)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinKapt)
    id(Plugins.kotlinSerialization)
    id(Plugins.mavenPublish)
}

group = Application.groupId
version = Application.version

android {
    buildFeatures.dataBinding = true
}

val android = listOf(
    common.serialization,
    common.reflect,
    androidLibs.coroutines,
    androidLibs.appCompat,
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
    api(common.merseyLib.kotlin.ext)
    api(common.merseyLib.time)
    android.forEach { lib -> implementation(lib) }
}

afterEvaluate {
    publishing.publications {
        create<MavenPublication>("release") {
            groupId = group.toString()
            artifactId = project.name
            version = rootProject.version.toString()
            from(components["release"])
        }
    }

    repositories {
        mavenCentral()
    }
}