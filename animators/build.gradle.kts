plugins {
    id(Plugins.androidConvention)
    id(Plugins.kotlinConvention)
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
    androidLibs.appCompat
)

dependencies {
    implementation(projects.utils)
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