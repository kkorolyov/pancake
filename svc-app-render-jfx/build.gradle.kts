plugins {
	id("common")
	id("kt")
	id("jfx")
	id("lib")
	id("testable")
}

description = "JavaFX Application and RenderMedium implementation"

dependencies {
	implementation(projects.platform)
}

javafx {
	modules("javafx.base", "javafx.graphics")
}

tasks.compileJava {
	options.compilerArgs.addAll(
		listOf(
			"--patch-module", "dev.kkorolyov.pancake.svc.apprender.jfx=${sourceSets.main.get().output.asPath}"
		)
	)
}
