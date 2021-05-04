plugins {
	id("common")
	groovy
}

description = "Test utilities"

repositories {
	mavenCentral()
}
dependencies {
	val spockVersion: String by project
	implementation("org.spockframework:spock-core:$spockVersion")

	implementation(projects.platform)
}
