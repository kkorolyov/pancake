plugins {
	kotlin("jvm")
	id("org.openjfx.javafxplugin")
	id("org.jetbrains.dokka")
	groovy
	`maven-publish`
}

description = "JavaFX-driven input and control implementation"

dependencies {
	implementation(libs.bundles.stdlib)
	implementation(libs.slf4j)

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
}
javafx {
	version = tasks.compileJava.get().targetCompatibility
	modules("javafx.graphics")
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
