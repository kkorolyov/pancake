plugins {
	configKotlin
	configLwjgl
	configPublish
	`java-test-fixtures`
}

description = "Common rendering system utilities"

dependencies {
	implementation(projects.platform)
	implementation(projects.core)

	testFixturesImplementation(libs.bundles.test)
	testFixturesImplementation(projects.platform)
}

val setupLwjgl: (Any) -> Unit by extra
setupLwjgl(listOf(libs.lwjgl.stb))
