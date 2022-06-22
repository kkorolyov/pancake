plugins {
	`java-library`
	groovy
	`maven-publish`
}

description = "Main Pancake engine platform"

dependencies {
	implementation(libs.bundles.stdlib)
	api(libs.flub)
	implementation(libs.snakeyaml)
	implementation(libs.jackson)

	testImplementation(libs.bundles.test)
	testImplementation(projects.testUtils)
}

publishing {
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
