plugins {
	`java-library`
}
apply(from = "../kotlin.gradle")
apply(from = "../publish.gradle.kts")

description = "Graphical debugging tools that can hook into a Pancake application"

dependencies {
	implementation(libs.bundles.stdlib)

	api(libs.imgui.binding)
	implementation(libs.imgui.lwjgl)

	implementation(projects.platform)
}

(extra["setupLwjgl"] as (Any) -> Unit)(listOf(libs.lwjgl.opengl, libs.lwjgl.glfw, libs.lwjgl.stb))
