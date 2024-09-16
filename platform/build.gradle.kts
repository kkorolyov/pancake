plugins {
	configPublish
	`java-test-fixtures`
}

description = "Main Pancake engine platform"

dependencies {
	api(libs.flub)
	implementation(libs.snakeyaml)
	implementation(libs.jackson)
	implementation(libs.jspecify)
	implementation(libs.classgraph)

	testFixturesImplementation(libs.bundles.test)
}
