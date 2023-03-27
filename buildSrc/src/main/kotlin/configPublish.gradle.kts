plugins {
	id("configBase")
	`java-library`
	`maven-publish`
}

java {
	withSourcesJar()
	if (!plugins.hasPlugin("org.jetbrains.dokka")) {
		withJavadocJar()
	}
}

publishing {
	publications {
		create<MavenPublication>("main") {
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
