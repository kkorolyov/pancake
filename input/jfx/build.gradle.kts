plugins {
	id("common")
	id("kt")
	id("jfx")
	id("lib")
	id("testable")
	id("nested")
}

description = "JavaFX-driven input and control implementation"

dependencies {
	api(projects.platform)
	implementation(projects.core)
}

javafx {
	modules("javafx.graphics")
}
