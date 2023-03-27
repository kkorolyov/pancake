plugins {
	configKotlin
	configLwjgl
	configPublish
}

description = "GLFW input system and control implementations"

dependencies {
	api(projects.platform)
	api(projects.input)
	implementation(projects.core)
}

val setupLwjgl: (Any) -> Unit by extra
setupLwjgl(listOf(libs.lwjgl.glfw))
