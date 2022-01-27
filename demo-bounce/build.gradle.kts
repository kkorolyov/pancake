plugins {
	kotlin("jvm")
	id("org.openjfx.javafxplugin")
	id("org.jetbrains.dokka")
	id("org.javamodularity.moduleplugin")
	application
}

description = "Demos collision physics"

dependencies {
	implementation(projects.demo)
}

tasks.compileKotlin {
	kotlinOptions {
		jvmTarget = tasks.compileJava.get().targetCompatibility
	}
}
javafx {
	version = tasks.compileJava.get().targetCompatibility
	modules("javafx.fxml", "javafx.web", "javafx.swing", "javafx.media")
}

application {
	mainModule.set("dev.kkorolyov.pancake.demo.bounce")
	mainClass.set("dev.kkorolyov.pancake.demo.bounce.LauncherKt")
}
