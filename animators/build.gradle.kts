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
    androidLibs.appCompat
)

dependencies {
    implementation(projects.utils)
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