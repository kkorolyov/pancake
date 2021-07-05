plugins {
	id("common")
	id("kt")
	id("jfx")
	id("nested")
	application
}

description = "Demo rendering controllable 2D rectangles"

dependencies {
	implementation(projects.platform)
	implementation(projects.core)
	implementation(projects.graphics.jfx)
	implementation(projects.audio.jfx)
	implementation(projects.input.jfx)

	val log4jVersion: String by project
	implementation("org.apache.logging.log4j:log4j-slf4j18-impl:$log4jVersion")
	val jacksonVersion: String by project
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
}

javafx {
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
