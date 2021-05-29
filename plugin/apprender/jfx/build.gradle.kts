plugins {
	id("common")
	id("kt")
	id("jfx")
	id("lib")
	id("testable")
}

group = "$group.plugin.apprender"
description = "JavaFX Application and RenderMedium implementation"

dependencies {
	implementation(projects.platform)
}

javafx {
	modules("javafx.base", "javafx.graphics")
}

tasks.jar {
	archiveBaseName.set("${parent?.name}-${project.name}")
}
