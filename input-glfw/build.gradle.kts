plugins {
	`java-library`
	kotlin("jvm")
	id("org.jetbrains.dokka")
	groovy
	`maven-publish`
}

description = "GLFW input system and control implementations"

dependencies {
	implementation(libs.bundles.stdlib)

	implementation(platform(libs.lwjgl.bom))
	implementation(libs.lwjgl)
	implementation(libs.lwjgl.glfw)

	api(projects.platform)
	api(projects.inputCommon)
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

subprojects {
	apply(plugin = "java-library")
	apply(plugin = "maven-publish")

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
		listOf(parent.libs.lwjgl.asProvider().get(), parent.libs.lwjgl.glfw.get()).forEach {
			implementation(variantOf(provider { it }) {
				classifier("natives-$name")
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
