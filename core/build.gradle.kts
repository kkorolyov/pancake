plugins {
	`java-library`
	groovy
}
apply(from = "$rootDir/publish.gradle.kts")

description = "Collection of general, reusable systems and components for the Pancake engine"

dependencies {
	implementation(libs.bundles.stdlib)

	implementation(projects.platform)

	testImplementation(libs.bundles.test)
	testImplementation(projects.testUtils)
}
