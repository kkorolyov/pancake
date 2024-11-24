plugins {
	configKotlin
	configPublish
	`java-test-fixtures`
}

description = "Common rendering system utilities"

dependencies {
	api(projects.platform)
	implementation(projects.core)

	testFixturesImplementation(libs.bundles.test)
}
