plugins {
	configPublish
	`java-test-fixtures`
}

description = "Main Pancake engine platform"

dependencies {
	api(libs.flub)
	implementation(libs.snakeyaml)
	implementation(libs.jackson)

	testFixturesImplementation(libs.bundles.test)
}
