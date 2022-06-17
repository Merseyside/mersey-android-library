enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    val group = "io.github.merseyside"
    val catalogVersions = "1.5.0"
    versionCatalogs {
        val androidLibs by creating {
            from("$group:catalog-version-android:$catalogVersions")
            version("dagger", "2.42")
        }

        val common by creating {
            from("$group:catalog-version-common:$catalogVersions")
        }

        val catalogPlugins by creating {
            from("$group:catalog-version-plugins:$catalogVersions")
        }
    }
}

include(":app")

include(":animators")
include(":adapters")
include(":utils")
include(":archy")

rootProject.name = "mersey-android-library"