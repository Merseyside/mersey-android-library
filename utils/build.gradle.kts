plugins {
    id(Plugins.androidConvention)
    id(Plugins.kotlinConvention)
    id(Plugins.kotlinKapt)
    id(Plugins.kotlinSerialization)
    `maven-publish-config`
}

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

//java {
//    withSourcesJar()
//}

afterEvaluate {
    publishing.publications {
        create<MavenPublication>("release") {
            groupId = Metadata.groupId
            artifactId = project.name
            version = Metadata.version
            from(components["release"])
        }
    }

    repositories {
        mavenCentral()
    }
}