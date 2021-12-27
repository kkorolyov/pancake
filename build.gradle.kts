plugins {
	id("org.jetbrains.dokka") version "1.+"
}

tasks.wrapper {
	distributionType = Wrapper.DistributionType.ALL
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
		it.tasks.withType<Javadoc>() + it.tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>()
			.filter { it.name == "dokkaHtml" }
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
			mavenContent {
				releasesOnly()
			}
			credentials {
				username = System.getenv("GITHUB_ACTOR")
				password = System.getenv("GITHUB_TOKEN")
			}
		}
	}

	dependencyLocking {
		lockAllConfigurations()
	}
}
