apply<JavaLibraryPlugin>()
apply<MavenPublishPlugin>()

configure<JavaPluginExtension> {
	withSourcesJar()
	if (!plugins.hasPlugin("org.jetbrains.dokka")) {
		withJavadocJar()
	}
}

configure<PublishingExtension> {
	publications {
		create<MavenPublication>("mvn") {
			from(components["java"])
		}
	}

	repositories {
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/kkorolyov/pancake")
			credentials {
				username = System.getenv("GITHUB_ACTOR")
				password = System.getenv("GITHUB_TOKEN")
			}
		}
	}
}
