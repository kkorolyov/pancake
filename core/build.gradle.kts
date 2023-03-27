plugins {
	configPublish
}

description = "Collection of general, reusable systems and components for the Pancake engine"

dependencies {
	implementation(projects.platform)

	testImplementation(testFixtures(projects.platform))
}
