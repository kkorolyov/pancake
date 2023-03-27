plugins {
	configKotlin
	configLwjgl
	configPublish
}

description = "OpenAL audio system and struct implementations"

dependencies {
	api(projects.platform)
	implementation(projects.core)
}

val setupLwjgl: (Any) -> Unit by extra
setupLwjgl(listOf(libs.lwjgl.openal))
