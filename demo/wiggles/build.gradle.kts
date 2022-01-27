plugins {
	kotlin("jvm")
	id("org.openjfx.javafxplugin")
	id("org.jetbrains.dokka")
	id("org.javamodularity.moduleplugin")
	application
}

description = "Demo of mouse-controlled physics"

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
	modules("javafx.fxml", "javafx.web", "javafx.swing")
}

application {
	mainModule.set("dev.kkorolyov.pancake.demo.wiggles")
	mainClass.set("dev.kkorolyov.pancake.demo.wiggles.LauncherKt")
}
