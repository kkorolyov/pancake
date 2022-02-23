plugins {
	kotlin("jvm")
	id("org.jetbrains.dokka")
	groovy
	`maven-publish`
}

description = "Common rendering system utilities"

dependencies {
	implementation(libs.bundles.stdlib)

	api(projects.platform)
	implementation(projects.core)

	testImplementation(libs.bundles.test)
}

java {
	withSourcesJar()
}

tasks.compileKotlin {
	kotlinOptions {
		jvmTarget = tasks.compileJava.get().targetCompatibility
	}
	destinationDirectory.set(tasks.compileJava.get().destinationDirectory)
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
