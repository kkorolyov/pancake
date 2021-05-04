plugins {
	id("common")
	id("lib")
	id("testable")
	`java-library`
}

description = "Main Pancake engine platform"

dependencies {
	val snakeyamlVersion: String by project
	implementation("org.yaml:snakeyaml:$snakeyamlVersion")

	testImplementation(projects.testUtils)
}
