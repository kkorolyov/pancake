plugins {
	configKotlin
	configPublish
}

description = "Common rendering system utilities"

dependencies {
	api(projects.platform)
	implementation(projects.core)
}
