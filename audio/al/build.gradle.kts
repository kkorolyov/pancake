plugins {
	`java-library`
	groovy
}
apply(from = "$rootDir/kotlin.gradle")
apply(from = "$rootDir/publish.gradle.kts")

description = "OpenAL audio system and struct implementations"

dependencies {
	api(projects.platform)
	implementation(projects.core)
}

(extra["setupLwjgl"] as (Any) -> Unit)(listOf(libs.lwjgl.openal))
