plugins {
	id("configBase")
	`java-library`
	`maven-publish`
}

java {
	withSourcesJar()
}

publishing {
	publications {
		create<MavenPublication>("main") {
			from(components["java"])
			versionMapping {
				allVariants {
					fromResolutionResult()
				}
			}
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
