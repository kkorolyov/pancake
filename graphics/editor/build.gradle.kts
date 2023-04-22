plugins {
	configKotlin
	configLwjgl
	configPublish
}

description = "Provides editor fragments for graphics components"

dependencies {
	implementation(projects.platform)
	implementation(projects.graphics)
	implementation(projects.editor)

	testImplementation(testFixtures(projects.platform))
	testImplementation(testFixtures(projects.editor))

	dependencyLocking {
		ignoredDependencies.add("io.github.spair:imgui-java-natives*")
	}
}
