plugins {
	id("common")
	id("lib")
	id("testable")
	`java-library`
}

description = "Collection of general, reusable systems and components for the Pancake engine"

dependencies {
	val snakeyamlVersion: String by project
	implementation("org.yaml:snakeyaml:$snakeyamlVersion")

	implementation(projects.platform)

	testImplementation(projects.testUtils)
}
