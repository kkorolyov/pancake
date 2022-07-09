plugins {
	`java-library`
	groovy
}
apply(from = "../kotlin.gradle")
apply(from = "../publish.gradle.kts")

description = "Common input system utilities"

dependencies {
	implementation(libs.bundles.stdlib)

	api(projects.platform)

	testImplementation(libs.bundles.test)
}
