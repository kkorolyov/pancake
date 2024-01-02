plugins {
	`java-library`
	`kotlin-dsl`
}

repositories {
	mavenCentral()
	maven("https://plugins.gradle.org/m2/")
}
dependencies {
	implementation(libs.gradle.kotlin)
	implementation(libs.gradle.dokka)
	implementation(libs.gradle.jmh)
}
