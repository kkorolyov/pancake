plugins {
	kotlin("jvm")
	id("org.jetbrains.dokka")
	groovy
	`maven-publish`
}

description = "OpenGL rendering system and renderable implementations"

val lwjglLibs = libs.lwjgl.run { listOf(asProvider(), opengl) }

dependencies {
	implementation(libs.bundles.stdlib)

	api(platform(libs.lwjgl.bom))
	lwjglLibs.forEach(::api)

	api(projects.platform)
	api(projects.graphicsCommon)
	implementation(projects.core)

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
		lwjglLibs.forEach {
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
