plugins {
	`java-library`
	groovy
}
apply(from = "../kotlin.gradle")
apply(from = "../publish.gradle.kts")

description = "GLFW input system and control implementations"

dependencies {
	implementation(libs.bundles.stdlib)

	api(projects.platform)
	api(projects.inputCommon)
	implementation(projects.core)

	testImplementation(libs.bundles.test)
}

(extra["setupLwjgl"] as (Any) -> Unit)(listOf(libs.lwjgl.glfw))
