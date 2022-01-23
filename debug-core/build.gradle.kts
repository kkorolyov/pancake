plugins {
	kotlin("jvm")
	id("org.openjfx.javafxplugin")
	id("org.jetbrains.dokka")
	id("org.javamodularity.moduleplugin")
}

description = "Provides debug fragments for Core components"

dependencies {
	implementation(libs.tornadofx)

	implementation(projects.platform)
	implementation(projects.core)
	implementation(projects.debug)
}

tasks.compileKotlin {
	kotlinOptions {
		jvmTarget = tasks.compileJava.get().targetCompatibility
	}
}
javafx {
	version = tasks.compileJava.get().targetCompatibility
	modules("javafx.graphics", "javafx.fxml", "javafx.web", "javafx.swing")
}
