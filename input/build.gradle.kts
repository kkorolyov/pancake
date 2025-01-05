plugins {
	configKotlin
	configPublish
	`java-test-fixtures`
}

description = "Common input system utilities"

dependencies {
	api(projects.platform)

	testFixturesImplementation(libs.bundles.test)
}
