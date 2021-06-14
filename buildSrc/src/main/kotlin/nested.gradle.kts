plugins {
	java
}

group = "$group.${parent?.name}"

tasks.jar {
	archiveBaseName.set("${parent?.name}-${project.name}")
}
