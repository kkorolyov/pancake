plugins {
	configKotlin
	configPublish
	`java-test-fixtures`
}

description = "Common input system utilities"

dependencies {
	implementation(projects.platform)

	testFixturesImplementation(libs.bundles.test)
	testFixturesImplementation(projects.platform)
}
