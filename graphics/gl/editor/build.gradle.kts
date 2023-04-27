plugins {
	configKotlin
	configLwjgl
	configPublish
}

description = "Provides editor fragments for OpenGL graphics components"

dependencies {
	implementation(projects.platform)
	implementation(projects.graphicsGl)
	implementation(projects.editor)
	implementation(projects.graphicsEditor)

	testImplementation(testFixtures(projects.platform))
	testImplementation(testFixtures(projects.editor))

	dependencyLocking {
		ignoredDependencies.add("io.github.spair:imgui-java-natives*")
	}
}
