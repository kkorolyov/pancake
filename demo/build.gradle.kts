plugins {
	kotlin("jvm")
	id("org.openjfx.javafxplugin")
	id("org.jetbrains.dokka")
	id("org.javamodularity.moduleplugin")
}

description = "Common demo skeleton"

dependencies {
	api(libs.bundles.stdlib)
	api(libs.tornadofx)
	api(libs.bundles.log)

	api(projects.platform)
	api(projects.core)
	api(projects.graphicsJfx)
	api(projects.audioJfx)
	api(projects.inputJfx)

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
