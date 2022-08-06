plugins {
	`java-library`
	groovy
}
apply(from = "$rootDir/kotlin.gradle")
apply(from = "$rootDir/publish.gradle.kts")

description = "OpenGL rendering system and renderable implementations"

dependencies {
	api(projects.platform)
	api(projects.graphics)
	implementation(projects.core)
}

(extra["setupLwjgl"] as (Any) -> Unit)(listOf(libs.lwjgl.opengl, libs.lwjgl.stb))
