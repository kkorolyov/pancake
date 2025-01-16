plugins {
	configKotlin
	configPublish
}

description = "Provides editor fragments for input components"

dependencies {
	implementation(projects.platform)
	implementation(projects.input)
	implementation(projects.editor)

	testImplementation(testFixtures(projects.platform))
	testImplementation(testFixtures(projects.editor))

	dependencyLocking {
		ignoredDependencies.add("io.github.spair:imgui-java-natives*")
	}
}
