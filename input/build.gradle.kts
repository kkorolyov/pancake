plugins {
	`java-library`
	groovy
}
apply(from = "$rootDir/kotlin.gradle")
apply(from = "$rootDir/publish.gradle.kts")

description = "Common input system utilities"

dependencies {
	implementation(libs.bundles.stdlib)

	api(projects.platform)

	testImplementation(libs.bundles.test)
}
