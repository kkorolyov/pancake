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
	implementation(projects.input.jfx)

	val log4jVersion: String by project
	implementation("org.apache.logging.log4j:log4j-slf4j18-impl:$log4jVersion")
	val jacksonVersion: String by project
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
}

application {
	mainModule.set("dev.kkorolyov.pancake.demo.wasdbox")
	mainClass.set("dev.kkorolyov.pancake.demo.wasdbox.LauncherKt")
}

javafx {
	modules("javafx.graphics", "javafx.controls")
}
