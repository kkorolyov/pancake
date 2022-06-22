plugins {
	`maven-publish`
	kotlin("jvm")
	id("org.jetbrains.dokka")
}

description = "Provides debug fragments for Core components"

val jfxLibs = libs.javafx.run { listOf(base, controls, graphics) }

dependencies {
	implementation(libs.tornadofx)

	jfxLibs.forEach {
		implementation(variantOf(it) {
			classifier("linux")
		})
	}

	implementation(projects.platform)
	implementation(projects.core)
	implementation(projects.editor)
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

subprojects {
	apply<JavaLibraryPlugin>()
	apply<MavenPublishPlugin>()

	val parent = parent!!

	description = "${parent.description} - $name variant"

	tasks.clean {
		doLast {
			projectDir.deleteRecursively()
		}
	}

	dependencyLocking {
		configurations.all { resolutionStrategy.deactivateDependencyLocking() }
	}

	dependencies {
		api(parent)
		jfxLibs.forEach {
			api(variantOf(it) {
				classifier(name)
			})
		}
	}

	publishing {
		publications {
			create<MavenPublication>("mvn") {
				from(components["java"])
				artifactId = "${parent.name}-${project.name}"
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
}
