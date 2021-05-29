plugins {
	id("common")
	id("kt")
	id("jfx")
	id("lib")
	id("testable")
	id("nested")
}

group = "$group.graphics"
description = "JavaFX drawable stamps and draw system implementations"

dependencies {
	api(projects.platform)
	implementation(projects.core)
}

javafx {
	modules("javafx.base", "javafx.graphics")
}
