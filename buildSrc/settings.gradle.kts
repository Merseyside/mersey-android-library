dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }

    val catalogVersions = "1.7.4"
    val group = "io.github.merseyside"
    versionCatalogs {

        val catalogGradle by creating {
            from("$group:catalog-version-gradle:$catalogVersions")
        }
    }
}