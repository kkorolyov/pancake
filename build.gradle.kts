tasks.register("allDeps") {
	group = "help"
	description = "List dependencies of all projects"

	dependsOn(subprojects.map { it.tasks.withType<DependencyReportTask>() })
}
