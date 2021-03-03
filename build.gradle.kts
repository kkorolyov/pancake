import org.gradle.api.JavaVersion.VERSION_14
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openjfx.gradle.JavaFXOptions

tasks.wrapper {
	distributionType = Wrapper.DistributionType.ALL
}

plugins {
	`java-library`
	groovy
	kotlin("jvm") version "1.4.31"
	id("org.jetbrains.dokka") version "1.4.20"
	id("org.openjfx.javafxplugin") version "0.0.9"
	`maven-publish`
}

description = "Extensible Java game engine with an entity-component-system architecture"

// For kotlin gradle plugins
repositories {
	jcenter()
}

tasks.withType<DependencyReportTask> {
	group = "help"
	description = "List dependencies of all projects"
	dependsOn(subprojects.map { it.tasks.withType<DependencyReportTask>() })
}

tasks.register("docs") {
	group = "documentation"
	description = "Bundles all subproject documentation together"

	val destination = "${project.buildDir}/docs"
	val subDocs = subprojects.map {
		it.tasks.withType<Javadoc>() + it.tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>()
			.filter { it.name == "dokkaHtml" }
	}.flatten()

	dependsOn(subDocs)
	doFirst {
		copy {
			from("docs")
			into(destination)
		}

		subDocs.forEach {
			copy {
				from(it)
				into("$destination/${it.project.name}")
			}
		}
	}
}

subprojects {
	group = "dev.kkorolyov"
	version = "0.1-SNAPSHOT"

	repositories {
		jcenter()  // Most things
		maven(url = "https://dl.bintray.com/kkorolyov/java")  // SimpleTools
		maven(url = "https://dl.bintray.com/kkorolyov/groovy")  // SimpleSpecs
	}

	dependencyLocking {
		lockAllConfigurations()
	}
}

// Projects
val platform: Project = project(":pancake-platform")
val core: Project = project(":pancake-core")
val testUtils: Project = project(":pancake-test-utils")

val javaFxApplication: Project = project(":javafx-application")
val javaFxAudio: Project = project(":javafx-audio")

val skillet: Project = project(":skillet")
val killstreek: Project = project(":killstreek")

// Testable
configure(
	listOf(
		platform,
		core,
		javaFxApplication,
		javaFxAudio,
		skillet,
		killstreek
	)
) {
	apply(plugin = "groovy")

	dependencies {
		val spockVersion: String by project
		val byteBuddyVersion: String by project
		val specsVersion: String by project

		testImplementation("org.spockframework:spock-core:$spockVersion")
		testImplementation("net.bytebuddy:byte-buddy:$byteBuddyVersion")
		testImplementation("dev.kkorolyov:simple-specs:$specsVersion")
		testImplementation(testUtils)
	}
}

// Libraries
configure(
	listOf(
		platform,
		core
	)
) {
	apply(plugin = "java-library")
	apply(plugin = "maven-publish")
	// TODO Replace with native "inferModulePath" when simple deps declare explicit module names
	apply(plugin = "org.javamodularity.moduleplugin")

	java {
		sourceCompatibility = VERSION_14
		targetCompatibility = VERSION_14

		withSourcesJar()
		withJavadocJar()

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
}

// Platform-reliant
configure(
	listOf(
		core,
		javaFxApplication,
		javaFxAudio,
		skillet,
		killstreek
	)
) {
	dependencies {
		implementation(platform)
	}
}
// Core-reliant
configure(listOf(killstreek)) {
	dependencies {
		implementation(core)
	}
}

// Kotlin
configure(
	listOf(
		javaFxApplication,
		javaFxAudio,
		killstreek,
		skillet
	)
) {
	apply(plugin = "kotlin")
	apply(plugin = "org.jetbrains.dokka")
	// TODO Replace with native "inferModulePath" when simple deps declare explicit module names
	apply(plugin = "org.javamodularity.moduleplugin")

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			jvmTarget = "14"
		}
	}

	tasks.withType<Javadoc> {
		enabled = false
	}
}

project(":pancake-platform") {
	description = "Main Pancake engine platform"

	dependencies {
		val propsVersion: String by project
		val filesVersion: String by project
		val funcsVersion: String by project
		val structsVersion: String by project

		// TODO Use slf4j
		api("dev.kkorolyov:simple-logs:3.+")
		api("dev.kkorolyov:simple-props:$propsVersion")
		api("dev.kkorolyov:simple-files:$filesVersion")
		api("dev.kkorolyov:simple-funcs:$funcsVersion")
		api("dev.kkorolyov:simple-structs:$structsVersion")
	}
}
project(":pancake-core") {
	description = "Collection of general, reusable systems and components for the Pancake engine"
}
project(":pancake-test-utils") {
	apply(plugin = "groovy")
	apply(plugin = "org.openjfx.javafxplugin")

	description = "Test utilities"

	dependencies {
		val spockVersion: String by project
		val specsVersion: String by project

		implementation("org.spockframework:spock-core:$spockVersion")
		implementation("dev.kkorolyov:simple-specs:$specsVersion")
		implementation(platform)
	}

	configure<JavaFXOptions> {
		version = "15.+"
		modules = listOf("javafx.controls")
	}
}

project(":javafx-application") {
	apply(plugin = "org.openjfx.javafxplugin")

	description = "JavaFX Application and RenderMedium implementation"

	configure<JavaFXOptions> {
		version = "15.+"
		modules = listOf("javafx.graphics")
	}
}
project(":javafx-audio") {
	apply(plugin = "org.openjfx.javafxplugin")

	description = "JavaFX AudioFactory implementation"

	configure<JavaFXOptions> {
		version = "15.+"
		modules = listOf("javafx.media")
	}
}

project(":skillet") {
	apply(plugin = "application")
	apply(plugin = "org.openjfx.javafxplugin")

	description = "Standalone application for designing and exporting entities"

	dependencies {
		implementation(kotlin("reflect"))
	}

	configure<JavaApplication> {
		mainClass.set("dev.kkorolyov.pancake.skillet.Skillet")
	}

	configure<JavaFXOptions> {
		version = "15.+"
		modules = listOf("javafx.controls")
	}
}

project(":killstreek") {
	apply(plugin = "application")

	description = "Top-down ARPG with dynamic RNG system"

	dependencies {
		implementation(javaFxApplication)
		implementation(javaFxAudio)
	}

	configure<JavaApplication> {
		mainClass.set("dev.kkorolyov.killstreek.LauncherKt")
	}

	tasks.named<JavaExec>("run") {
		dependsOn("installDist")

		// Launch along with resources
		workingDir = tasks.named<Sync>("installDist").get().destinationDir
	}
}
