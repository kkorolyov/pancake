plugins {
	java
}

tasks.jar {
	archiveBaseName.set("${parent?.name}-${project.name}")
}
