plugins {
	`java-library`
	groovy
}
apply(from = "$rootDir/publish.gradle.kts")

description = "Collection of general, reusable systems and components for the Pancake engine"

dependencies {
	implementation(projects.platform)

	testImplementation(testFixtures(projects.platform))
}
