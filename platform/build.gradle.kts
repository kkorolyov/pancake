plugins {
	configPublish
	`java-test-fixtures`
}

description = "Main Pancake engine platform"

dependencies {
	api(libs.flub)
	implementation(libs.jackson)

	testFixturesImplementation(libs.bundles.test)
}
