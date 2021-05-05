plugins {
	id("common")
	id("kt")
	id("jfx")
	id("lib")
	id("testable")
}

group = "$group.plugin.app-render"
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
tasks.compileJava {
	options.compilerArgs.addAll(
		listOf(
			"--patch-module", "dev.kkorolyov.pancake.plugin.apprender.jfx=${sourceSets.main.get().output.asPath}"
		)
	)
}
