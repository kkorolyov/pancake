plugins {
	kotlin("jvm")
	id("org.jetbrains.dokka")
	groovy
	`maven-publish`
}

description = "Common input system utilities"

dependencies {
	implementation(libs.bundles.stdlib)

	api(projects.platform)

	testImplementation(libs.bundles.test)
}

tasks.compileKotlin {
	kotlinOptions {
		jvmTarget = tasks.compileJava.get().targetCompatibility
	}
	destinationDirectory.set(tasks.compileJava.get().destinationDirectory)
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
