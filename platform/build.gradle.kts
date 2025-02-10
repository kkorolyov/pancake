plugins {
	configPublish
	`java-test-fixtures`
}

description = "Main Pancake engine platform"

dependencies {
	api(libs.flub)
	implementation(libs.jackson)
	implementation(libs.classgraph)

	testFixturesImplementation(libs.bundles.test)
}
