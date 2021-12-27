plugins {
	kotlin("jvm")
	id("org.openjfx.javafxplugin")
	id("org.jetbrains.dokka")
	id("org.javamodularity.moduleplugin")
	application
}

description = "Demo rendering controllable 2D rectangles"

dependencies {
	implementation(libs.bundles.stdlib)
	implementation(libs.bundles.log)

	implementation(projects.platform)
	implementation(projects.core)
	implementation(projects.graphicsJfx)
	implementation(projects.audioJfx)
	implementation(projects.inputJfx)
}

tasks.compileKotlin {
	kotlinOptions {
		jvmTarget = tasks.compileJava.get().targetCompatibility
	}
}
javafx {
	version = tasks.compileJava.get().targetCompatibility
	modules("javafx.graphics", "javafx.controls", "javafx.media")
}

application {
	mainModule.set("dev.kkorolyov.pancake.demo.wasdbox")
	mainClass.set("dev.kkorolyov.pancake.demo.wasdbox.LauncherKt")
}
tasks.named<JavaExec>("run") {
	dependsOn("installDist")

	// Launch alongside loose resources
	workingDir = tasks.named<Sync>("installDist").get().destinationDir
}
