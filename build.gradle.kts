import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
	java
	id("org.ajoberstar.reckon") version "0.+"
	kotlin("jvm") version "1.6.+" apply false
	id("org.jetbrains.dokka") version "1.6.21" apply false
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

	val destination = "$projectDir/docs"
	val subDocs = subprojects.map {
		it.tasks.withType<DokkaTask>().filter { it.name == "dokkaHtml" }.ifEmpty { it.tasks.withType<Javadoc>() }
	}.flatten()

	dependsOn(subDocs)
	doLast {
		subDocs.forEach {
			copy {
				from(it)
				into("$destination/${it.project.name}")
			}
		}
		// generate index
		File("$destination/index.html").writeText(
			"""
			<!DOCTYPE HTML>
			<html meta lang="en">
			
			<head>
				<meta charset="UTF-8">
				<meta name="viewport" content="width=device-width, initial-scale=1.0">
			
				<title>Pancake Javadoc</title>
			
				<link rel="shortcut icon" type="image/png" href="pancake.png">
			
				<style>
					table {
						font-size: 2em;
					}
			
					table>caption {
						display: flex;
						align-items: center;
					}
			
					td {
						background-color: #d38d5f;
			
						transition: background-color .5s;
					}
					td:hover {
						background-color: #e9c6af;
					}
			
					a {
						display: block;
						padding: 16px;
			
						text-decoration: none;
						color: inherit;
					}
				</style>
			</head>
			
			<body>
				<table>
					<caption>
						<img src="pancake.png" alt="pancake"> Modules
					</caption>
					<tr>
						${File(destination).listFiles()?.filter { it.isDirectory }?.map { it.name }?.joinToString("") { "<td><a href=\"$it/index.html\">$it</a></td>" } ?: ""}
					</tr>
				</table>
			</body>
			
			</html>
			""".trimIndent()
		)
	}
}

tasks.clean {
	doFirst {
		File("$projectDir/docs").listFiles { _, name -> name != "pancake.png" }?.let { delete(*it) }
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
