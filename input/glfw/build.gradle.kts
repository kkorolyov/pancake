plugins {
	`java-library`
	groovy
}
apply(from = "$rootDir/kotlin.gradle")
apply(from = "$rootDir/publish.gradle.kts")

description = "GLFW input system and control implementations"

dependencies {
	api(projects.platform)
	api(projects.input)
	implementation(projects.core)
}

(extra["setupLwjgl"] as (Any) -> Unit)(listOf(libs.lwjgl.glfw))
