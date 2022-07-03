plugins {
	`java-library`
	groovy
}
apply(from = "../kotlin.gradle")
apply(from = "../publish.gradle.kts")
apply(from = "../lwjgl.gradle.kts")

description = "OpenAL audio system and struct implementations"

val lwjglLibs = (extra["lwjglExpand"] as (Any) -> List<Any>)(libs.lwjgl.run { listOf(asProvider(), openal, stb) })
dependencies {
	implementation(libs.bundles.stdlib)

	lwjglLibs.forEach(::api)

	api(projects.platform)
	implementation(projects.core)

	testImplementation(libs.bundles.test)
}
