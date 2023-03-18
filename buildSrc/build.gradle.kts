plugins {
	`java-library`
	`kotlin-dsl`
}

repositories {
	mavenCentral()
}
dependencies {
	implementation(libs.gradle.kotlin)
	implementation(libs.gradle.dokka)
}
