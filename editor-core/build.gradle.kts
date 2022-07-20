plugins {
	`java-library`
}
apply(from = "../kotlin.gradle")
apply(from = "../publish.gradle.kts")

description = "Provides debug fragments for Core components"

dependencies {
	implementation(libs.bundles.stdlib)

	implementation(libs.imgui.binding)
	implementation(libs.imgui.lwjgl)

	implementation(projects.platform)
	implementation(projects.core)
	implementation(projects.editor)
}

(extra["setupLwjgl"] as (Any) -> Unit)(listOf(libs.lwjgl.opengl, libs.lwjgl.glfw, libs.lwjgl.stb))
