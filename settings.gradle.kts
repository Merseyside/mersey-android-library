enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

private val isLocalKotlinExtLibrary = false

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    val group = "io.github.merseyside"
    val catalogVersions = "1.3.5"
    versionCatalogs {
        val androidLibs by creating {
            from("$group:catalog-version-android:$catalogVersions")
        }

        val common by creating {
            from("$group:catalog-version-common:$catalogVersions")
        }
    }
}

include(":app")

include(":animators")
include(":adapters")
include(":utils")
include(":archy")

if (isLocalKotlinExtLibrary) {
    include(":kotlin-ext")
    project(":kotlin-ext").projectDir =
        File(rootDir.parent, "mersey-kotlin-ext/kotlin-ext")
}

rootProject.name = "mersey-android-library"