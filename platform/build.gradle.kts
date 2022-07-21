plugins {
	`java-library`
	groovy
}
apply(from = "$rootDir/publish.gradle.kts")

description = "Main Pancake engine platform"

dependencies {
	implementation(libs.bundles.stdlib)
	api(libs.flub)
	implementation(libs.snakeyaml)
	implementation(libs.jackson)

	testImplementation(libs.bundles.test)
	testImplementation(projects.testUtils)
}
