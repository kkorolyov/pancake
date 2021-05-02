import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.wrapper {
	distributionType = Wrapper.DistributionType.ALL
}

/**
 * Runner's OS
 */
val os: String = DefaultNativePlatform.getCurrentOperatingSystem().run {
	if (isWindows) {
		"win"
	} else if (isMacOsX) {
		"mac"
	} else if (isLinux) {
		"linux"
	} else {
		"unknown"
	}
}

plugins {
	`java-library`
	groovy
	kotlin("jvm") version "1.5.0"
	application
	id("org.jetbrains.dokka") version "1.4.20"
	`maven-publish`
	idea
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
	apply(plugin = "java")

	group = "dev.kkorolyov.pancake"

	repositories {
		jcenter()
		mavenCentral()
	}

	dependencies {
		// observability
		val slf4jVersion: String by project

		implementation("org.slf4j:slf4j-api:$slf4jVersion")

		dependencyLocking {
			lockAllConfigurations()
		}
	}

	// FIXME API dependencies not added to compile path in downstream lockfiles
	repositories {
		maven {
			url = uri("https://maven.pkg.github.com/kkorolyov/flopple")
			credentials {
				username = System.getenv("GITHUB_ACTOR")
				password = System.getenv("GITHUB_TOKEN")
			}
		}
	}

	dependencies {
		// stdlib
		val floppleVersion: String by project
		implementation("dev.kkorolyov:flopple:$floppleVersion")
	}

	java {
		sourceCompatibility = JavaVersion.VERSION_14
		targetCompatibility = JavaVersion.VERSION_14

		withSourcesJar()
		withJavadocJar()

		modularity.inferModulePath.set(true)
	}
}

// Projects
val platform: Project = project(":pancake-platform")
val core: Project = project(":pancake-core")
val testUtils: Project = project(":pancake-test-utils")

val javaFxApplication: Project = project(":javafx-application")
val javaFxAudio: Project = project(":javafx-audio")

val ponk: Project = project(":ponk")

val killstreek: Project = project(":killstreek")

// Testable
configure(
	listOf(
		platform,
		core,
		javaFxApplication,
		javaFxAudio,
		killstreek
	)
) {
	apply(plugin = "groovy")

	dependencies {
		val spockVersion: String by project
		val byteBuddyVersion: String by project

		testImplementation("org.spockframework:spock-core:$spockVersion")
		testImplementation("net.bytebuddy:byte-buddy:$byteBuddyVersion")
		testImplementation(testUtils)
	}

	tasks.test {
		useJUnitPlatform()
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

	dependencies {
		val jacksonVersion: String by project
		implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
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
		ponk,
		killstreek
	)
) {
	dependencies {
		implementation(platform)
	}
}
// Core-reliant
configure(
	listOf(
		ponk,
		killstreek
	)
) {
	dependencies {
		implementation(core)
	}
}

// Kotlin
configure(
	listOf(
		javaFxApplication,
		javaFxAudio,
		ponk,
		killstreek
	)
) {
	apply(plugin = "kotlin")
	apply(plugin = "org.jetbrains.dokka")

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
}
project(":pancake-core") {
	description = "Collection of general, reusable systems and components for the Pancake engine"
}
project(":pancake-test-utils") {
	apply(plugin = "groovy")

	description = "Test utilities"

	dependencies {
		val spockVersion: String by project
		val javaFxVersion: String by project

		implementation("org.spockframework:spock-core:$spockVersion")
		implementation("org.openjfx:javafx-controls:$javaFxVersion:$os")
		implementation(platform)
	}
}

project(":javafx-application") {
	description = "JavaFX Application and RenderMedium implementation"

	dependencies {
		val javaFxVersion: String by project
		implementation("org.openjfx:javafx-base:$javaFxVersion:$os")
		implementation("org.openjfx:javafx-graphics:$javaFxVersion:$os")
	}

	tasks.compileJava {
		options.compilerArgs.addAll(
			listOf(
				"--patch-module", "dev.kkorolyov.pancake.javafx.application=${sourceSets.main.get().output.asPath}"
			)
		)
	}
}
project(":javafx-audio") {
	description = "JavaFX AudioFactory implementation"

	dependencies {
		val javaFxVersion: String by project
		implementation("org.openjfx:javafx-base:$javaFxVersion:$os")
		implementation("org.openjfx:javafx-graphics:$javaFxVersion:$os")
		implementation("org.openjfx:javafx-media:$javaFxVersion:$os")
	}

	tasks.compileJava {
		options.compilerArgs.addAll(
			listOf(
				"--patch-module", "dev.kkorolyov.pancake.javafx.audio=${sourceSets.main.get().output.asPath}"
			)
		)
	}
}

project(":ponk") {
	apply(plugin = "application")

	description = "Simple pong-like"

	dependencies {
		val log4jVersion: String by project
		val jacksonVersion: String by project

		implementation("org.apache.logging.log4j:log4j-slf4j18-impl:$log4jVersion")
		implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")

		runtimeOnly(javaFxApplication)
		runtimeOnly(javaFxAudio)
	}

	tasks.compileJava {
		options.compilerArgs.addAll(
			listOf(
				"--patch-module", "dev.kkorolyov.ponk=${sourceSets.main.get().output.asPath}"
			)
		)
	}

	application {
		mainModule.set("dev.kkorolyov.ponk")
		mainClass.set("dev.kkorolyov.ponk.LauncherKt")
	}

}

project(":killstreek") {
	apply(plugin = "application")

	description = "Top-down ARPG with dynamic RNG system"

	dependencies {
		val log4jVersion: String by project
		val jacksonVersion: String by project

		implementation("org.apache.logging.log4j:log4j-slf4j18-impl:$log4jVersion")
		implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")

		runtimeOnly(javaFxApplication)
		runtimeOnly(javaFxAudio)
	}

	tasks.compileJava {
		options.compilerArgs.addAll(
			listOf(
				"--patch-module", "dev.kkorolyov.killstreek=${sourceSets.main.get().output.asPath}"
			)
		)
	}

	application {
		mainModule.set("dev.kkorolyov.killstreek")
		mainClass.set("dev.kkorolyov.killstreek.LauncherKt")
	}

	tasks.named<JavaExec>("run") {
		dependsOn("installDist")

		// Launch alongside loose resources
		workingDir = tasks.named<Sync>("installDist").get().destinationDir
	}
}
