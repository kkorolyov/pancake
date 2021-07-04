plugins {
	id("common")
	id("kt")
	id("jfx")
	id("lib")
	id("testable")
	id("nested")
}

description = "JavaFX audio component and system implementations"

dependencies {
	implementation(projects.platform)
	implementation(projects.core)
}

javafx {
	modules("javafx.media")
}
