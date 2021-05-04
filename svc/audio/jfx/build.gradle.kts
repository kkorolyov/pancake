plugins {
	id("common")
	id("kt")
	id("jfx")
	id("lib")
	id("testable")
}

group = "$group.svc.audio"
description = "JavaFX AudioFactory implementation"

dependencies {
	implementation(projects.platform)
}

javafx {
	modules("javafx.base", "javafx.graphics", "javafx.media")
}

tasks.compileJava {
	options.compilerArgs.addAll(
		listOf(
			"--patch-module", "dev.kkorolyov.pancake.svc.audio.jfx=${sourceSets.main.get().output.asPath}"
		)
	)
}
