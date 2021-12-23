plugins {
	`java-library`
	groovy
	`maven-publish`
}

description = "Collection of general, reusable systems and components for the Pancake engine"

dependencies {
	implementation(libs.bundles.stdlib)
	implementation(libs.slf4j)
	implementation(libs.snakeyaml)

	implementation(projects.platform)

	testImplementation(libs.bundles.test)
	testImplementation(projects.testUtils)
}

java {
	withSourcesJar()
	withJavadocJar()
}

tasks.test {
	useJUnitPlatform()
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
