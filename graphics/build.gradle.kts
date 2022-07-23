plugins {
	`java-library`
	groovy
}
apply(from = "$rootDir/kotlin.gradle")
apply(from = "$rootDir/publish.gradle.kts")

description = "Common rendering system utilities"

dependencies {
	api(projects.platform)
	implementation(projects.core)
}
