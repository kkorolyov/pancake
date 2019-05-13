import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig
import dev.kkorolyov.FullDocExtension
import org.gradle.api.JavaVersion.VERSION_11
import org.openjfx.gradle.JavaFXOptions

buildscript {
	repositories {
		maven(url = "https://dl.bintray.com/kkorolyov/groovy")  // FullDoc
	}
	dependencies {
		classpath("dev.kkorolyov:full-doc:1.+")
	}
}
plugins {
	`java-library`
	groovy
	kotlin("jvm") version "1.3.31"
	id("org.openjfx.javafxplugin") version "0.0.7"

	`maven-publish`
	id("com.jfrog.bintray") version "1.8.0"

	id("nebula.dependency-lock") version "5.0.6"
}
apply(plugin = "dev.kkorolyov.full-doc")

description = "Extensible Java game engine with an entity-component-system architecture"

// For kotlin gradle plugin
repositories {
	jcenter()
}

configure<FullDocExtension> {
	src = "docs"
	out = "${project.buildDir}/docs"
}

java {
	sourceCompatibility = VERSION_11
}

subprojects {
	apply(plugin = "nebula.dependency-lock")

	group = "dev.kkorolyov"
	version = "0.1"

	repositories {
		mavenLocal()  // FIXME For early dev only
		jcenter()  // Most things
		maven(url = "https://dl.bintray.com/kkorolyov/java")  // SimpleTools
		maven(url = "https://dl.bintray.com/kkorolyov/groovy")  // SimpleSpecs
	}
}

// Projects
val platform: Project = project(":pancake-platform")
val testUtils: Project = project(":pancake-test-utils")
val core: Project = project(":pancake-core")
val skillet: Project = project(":skillet")
val killstreek: Project = project(":killstreek")

// Testable
configure(
		listOf(
				platform,
				core,
				skillet,
				killstreek
		)
) {
	apply(plugin = "groovy")

	dependencies {
		testImplementation("org.spockframework:spock-core:1.1-groovy-2.+")
		testImplementation("cglib:cglib-nodep:3.+")
		testImplementation("org.objenesis:objenesis:1.+")
		testImplementation("dev.kkorolyov:simple-specs:1.+")
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
	apply(plugin = "com.jfrog.bintray")

	// Artifacts
	tasks.register<Jar>("sourceJar") {
		archiveClassifier.set("source")
		from(sourceSets.main.get().allJava)
	}
	tasks.register<Jar>("javadocJar") {
		archiveClassifier.set("javadoc")
		from(tasks.javadoc.get().destinationDir)
	}

	// Maven publish
	publishing {
		publications {
			create<MavenPublication>("pancake") {
				from(components["java"])

				artifact(tasks["sourceJar"])
				artifact(tasks["javadocJar"])
			}
		}
	}
	// Bintray upload
	afterEvaluate {
		bintray {
			user = project.findProperty("bintrayUser") as? String
			key = project.findProperty("bintrayKey") as? String

			setPublications("pancake")
			publish = true
			override = true

			pkg(delegateClosureOf<PackageConfig> {
				repo = "java"
				name = this@configure.name
				setLicenses("BSD 3-Clause")
				vcsUrl = "https://github.com/kkorolyov/Pancake.git"
				setVersion(this@configure.version)
			})
		}
	}
}

// Platform-reliant
configure(
		listOf(
				core,
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

project(":pancake-platform") {
	apply(plugin = "org.openjfx.javafxplugin")

	description = "Main Pancake engine platform"

	dependencies {
		api("dev.kkorolyov:simple-logs:3.+")
		api("dev.kkorolyov:simple-props:4.+")
		api("dev.kkorolyov:simple-files:1.+")
		api("dev.kkorolyov:simple-funcs:1.+")
		api("dev.kkorolyov:simple-structs:2.+")
	}

	configure<JavaFXOptions> {
		version = "11.+"
		modules = listOf("javafx.controls", "javafx.media")
	}
}
project(":pancake-test-utils") {
	apply(plugin = "groovy")
	apply(plugin = "org.openjfx.javafxplugin")

	description = "Test utilities"

	dependencies {
		implementation("org.spockframework:spock-core:1.1-groovy-2.+")
		implementation("dev.kkorolyov:simple-specs:1.+")
		implementation(platform)
	}

	configure<JavaFXOptions> {
		version = "11.+"
		modules = listOf("javafx.controls")
	}
}

project(":pancake-core") {
	apply(plugin = "org.openjfx.javafxplugin")

	description = "Collection of general, reusable systems and components for the Pancake engine"

	configure<JavaFXOptions> {
		version = "11.+"
		modules = listOf("javafx.graphics", "javafx.media")
	}
}

project(":skillet") {
	apply(plugin = "kotlin")
	apply(plugin = "application")
	apply(plugin = "org.openjfx.javafxplugin")

	dependencies {
		implementation(kotlin("stdlib-jdk8"))
		implementation(kotlin("reflect"))
	}

	description = "Standalone application for designing and exporting entities"

	configure<JavaApplication> {
		mainClassName = "dev.kkorolyov.pancake.skillet.Skillet"
	}

	configure<JavaFXOptions> {
		version = "11.+"
		modules = listOf("javafx.controls")
	}
}

project(":killstreek") {
	apply(plugin = "java")
	apply(plugin = "application")
	apply(plugin = "org.openjfx.javafxplugin")

	description = "Top-down ARPG with dynamic RNG system"

	configure<JavaApplication> {
		mainClassName = "dev.kkorolyov.killstreek/dev.kkorolyov.killstreek.Launcher"

		applicationDefaultJvmArgs = listOf("-Xmx4G")
	}

	configure<JavaFXOptions> {
		version = "11.+"
		modules = listOf("javafx.media")
	}

	tasks.register<Exec>("exec") {
		dependsOn("installDist")

		workingDir = tasks.named<Sync>("installDist").get().destinationDir
		commandLine = listOf("sh", "bin/killstreek")

		description = "Executes the distribution"
		group = "distribution"
	}
}
