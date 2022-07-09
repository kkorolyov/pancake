plugins {
	`java-library`
	groovy
}
apply(from = "../kotlin.gradle")
apply(from = "../publish.gradle.kts")

description = "Common rendering system utilities"

dependencies {
	implementation(libs.bundles.stdlib)

	api(projects.platform)
	implementation(projects.core)

	testImplementation(libs.bundles.test)
}
