plugins {
	`java-library`
	`java-test-fixtures`
	groovy
}
apply(from = "$rootDir/publish.gradle.kts")

description = "Main Pancake engine platform"

dependencies {
	api(libs.flub)
	implementation(libs.snakeyaml)
	implementation(libs.jackson)

	testFixturesImplementation(libs.bundles.test)
}
