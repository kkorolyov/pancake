plugins {
	`java-library`
	groovy
}
apply(from = "$rootDir/kotlin.gradle")
apply(from = "$rootDir/publish.gradle.kts")

description = "OpenAL audio system and struct implementations"

dependencies {
	implementation(libs.bundles.stdlib)

	api(projects.platform)
	implementation(projects.core)

	testImplementation(libs.bundles.test)
}

(extra["setupLwjgl"] as (Any) -> Unit)(listOf(libs.lwjgl.openal))
