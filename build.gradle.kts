import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL

tasks.wrapper {
	val wrapperVersion: String by project
	gradleVersion = wrapperVersion
	distributionType = ALL

	val wrapperSHA: String by project
	distributionSha256Sum = wrapperSHA
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
