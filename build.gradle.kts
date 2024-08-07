import org.jetbrains.dokka.gradle.DokkaTask

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

	notCompatibleWithConfigurationCache("enumerates subproject task data")

	val destination = "${project.layout.buildDirectory.get()}/docs"
	val subDocs = subprojects.map {
		it.tasks.withType<DokkaTask>().filter { it.name == "dokkaHtml" }.ifEmpty { it.tasks.withType<Javadoc>() }
	}.flatten()

	dependsOn(subDocs)
	doLast {
		copy {
			from("$projectDir/docs")
			include("pancake.png")
			into(destination)
		}
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
