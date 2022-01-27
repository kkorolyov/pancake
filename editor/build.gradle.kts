plugins {
	kotlin("jvm")
	id("org.openjfx.javafxplugin")
	id("org.jetbrains.dokka")
	id("org.javamodularity.moduleplugin")
}

description = "Graphical debugging tools that can hook into a Pancake application"

dependencies {
	implementation(libs.bundles.stdlib)
	implementation(libs.tornadofx)
	implementation(libs.slf4j)

	implementation(projects.platform)
	implementation(projects.core)
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
