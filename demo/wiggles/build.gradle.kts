plugins {
	kotlin("jvm")
	id("org.openjfx.javafxplugin")
	id("org.jetbrains.dokka")
	id("org.javamodularity.moduleplugin")
	application
}

description = "Demo of mouse-controlled physics"

dependencies {
	implementation(libs.bundles.stdlib)
	implementation(libs.tornadofx)
	implementation(libs.bundles.log)

	implementation(projects.platform)
	implementation(projects.core)
	implementation(projects.graphicsJfx)
	implementation(projects.audioJfx)
	implementation(projects.inputJfx)

	implementation(projects.editor)
	implementation(projects.editorCore)
}

tasks.compileKotlin {
	kotlinOptions {
		jvmTarget = tasks.compileJava.get().targetCompatibility
	}
}
javafx {
	version = tasks.compileJava.get().targetCompatibility
	modules("javafx.fxml", "javafx.web", "javafx.swing")
}

application {
	mainModule.set("dev.kkorolyov.pancake.demo.wiggles")
	mainClass.set("dev.kkorolyov.pancake.demo.wiggles.LauncherKt")
}
