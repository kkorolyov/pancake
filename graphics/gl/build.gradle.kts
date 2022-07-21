plugins {
	`java-library`
	groovy
}
apply(from = "$rootDir/kotlin.gradle")
apply(from = "$rootDir/publish.gradle.kts")

description = "OpenGL rendering system and renderable implementations"

dependencies {
	implementation(libs.bundles.stdlib)

	api(projects.platform)
	api(projects.graphics)
	implementation(projects.core)

	testImplementation(libs.bundles.test)
}

(extra["setupLwjgl"] as (Any) -> Unit)(listOf(libs.lwjgl.opengl))
