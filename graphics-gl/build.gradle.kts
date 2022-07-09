plugins {
	`java-library`
	groovy
}
apply(from = "../kotlin.gradle")
apply(from = "../publish.gradle.kts")

description = "OpenGL rendering system and renderable implementations"

dependencies {
	implementation(libs.bundles.stdlib)

	api(projects.platform)
	api(projects.graphicsCommon)
	implementation(projects.core)

	testImplementation(libs.bundles.test)
}

(extra["setupLwjgl"] as (Any) -> Unit)(listOf(libs.lwjgl.opengl))
