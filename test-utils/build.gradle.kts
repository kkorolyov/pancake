plugins {
	groovy
}

description = "Test utilities"

repositories {
	mavenCentral()
}
dependencies {
	implementation(libs.spock)
	implementation(projects.platform)
}
