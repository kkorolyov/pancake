plugins {
	`maven-publish`
	kotlin("jvm")
	id("org.openjfx.javafxplugin")
	id("org.jetbrains.dokka")
	id("org.javamodularity.moduleplugin")
}

description = "Provides debug fragments for Core components"

dependencies {
	implementation(libs.tornadofx)

	implementation(projects.platform)
	implementation(projects.core)
	implementation(projects.editor)
}

tasks.compileKotlin {
	kotlinOptions {
		jvmTarget = tasks.compileJava.get().targetCompatibility
	}
}
javafx {
	version = tasks.compileJava.get().targetCompatibility
	modules("javafx.graphics", "javafx.fxml", "javafx.web", "javafx.swing")
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
