import com.android.build.gradle.LibraryExtension
//import org.gradle.jvm.tasks.Jar

plugins {
    id("maven-publish-config")
}

afterEvaluate {
    val androidSourcesJar = tasks.create("androidSourcesJar", Jar::class) {
        archiveClassifier.set("sources")
        val androidExtension: LibraryExtension =
            this.project.extensions.getByType(LibraryExtension::class)
        from(androidExtension.sourceSets.getByName("main").java.srcDirs)
    }

    publishing.publications {
        create("release", MavenPublication::class.java) {
            from(components.getByName("release"))
            artifact(androidSourcesJar)
        }
    }

}

tasks.withType<GenerateModuleMetadata> {
    dependsOn(tasks.get("androidSourcesJar"))
    enabled = true
}