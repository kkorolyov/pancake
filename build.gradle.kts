import org.jetbrains.dokka.gradle.DokkaTask

plugins {
	java
	id("org.ajoberstar.reckon") version "0.+"
	kotlin("jvm") version "1.+" apply false
	id("org.jetbrains.dokka") version "1.6.10" apply false
	id("org.openjfx.javafxplugin") version "0.+" apply false
	id("org.javamodularity.moduleplugin") version "1.+" apply false
}

tasks.wrapper {
	distributionType = Wrapper.DistributionType.ALL
}

reckon {
	stages("rc", "final")
	setScopeCalc(calcScopeFromProp())
	setStageCalc(calcStageFromProp())
}
tasks.reckonTagCreate {
	dependsOn(tasks.check)
}

tasks.register("allDeps") {
	group = "help"
	description = "List dependencies of all projects"

	dependsOn(subprojects.map { it.tasks.withType<DependencyReportTask>() })
}

tasks.register("allDocs") {
	group = "documentation"
	description = "Bundles all project documentation together"

	val destination = "${project.buildDir}/docs"
	val subDocs = subprojects.map {
		it.tasks.withType<DokkaTask>().filter { it.name == "dokkaHtml" }.ifEmpty { it.tasks.withType<Javadoc>() }
	}.flatten()

	dependsOn(subDocs)
	doLast {
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
	repositories {
		mavenCentral()
		maven {
			url = uri("https://maven.pkg.github.com/kkorolyov/flub")
			credentials {
				username = System.getenv("GITHUB_ACTOR")
				password = System.getenv("GITHUB_TOKEN")
			}
		}
		maven {
			url = uri("https://oss.sonatype.org/content/repositories/snapshots")
			mavenContent {
				includeGroup("no.tornado")
			}
		}
	}

	dependencyLocking {
		lockAllConfigurations()
	}
}
