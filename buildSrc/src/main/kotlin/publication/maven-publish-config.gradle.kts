import java.util.*

plugins {
    id("javadoc-stub-convention")
    `maven-publish`
    signing
}

publishing {
    publications {
        withType<MavenPublication>().all {
            val projectGitUrl = "https://github.com/Merseyside/mersey-android-library"
            pom {
                name.set("Mersey android library")
                description.set("Contains animators, architecture features and some utils")
                url.set(projectGitUrl)

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("Merseyside")
                        name.set("Ivan Sablin")
                        email.set("ivanklessablin@gmail.com")
                    }
                }

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                issueManagement {
                    system.set("GitHub")
                    url.set("$projectGitUrl/issues")
                }

                scm {
                    connection.set("scm:git:$projectGitUrl")
                    developerConnection.set("scm:git:$projectGitUrl")
                    url.set(projectGitUrl)
                }
            }
        }
    }
}

signing {
    val signingKeyId: String? = System.getenv("SIGNING_KEY_ID")
    val signingPassword: String? = System.getenv("SIGNING_PASSWORD")
    val signingKey: String? = System.getenv("SIGNING_KEY")?.let { base64Key ->
        val _base = base64Key.replace("\n", "")
        String(Base64.getDecoder().decode(_base))
    }
    if (signingKeyId != null) {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(publishing.publications)
    }
}