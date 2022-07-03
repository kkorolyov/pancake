plugins {
	`java-library`
	groovy
}
apply(from = "../kotlin.gradle")
apply(from = "../publish.gradle.kts")
apply(from = "../lwjgl.gradle.kts")

description = "OpenGL rendering system and renderable implementations"

val lwjglLibs = (extra["lwjglExpand"] as (Any) -> List<Any>)(libs.lwjgl.run { listOf(asProvider(), opengl) })
dependencies {
	implementation(libs.bundles.stdlib)

	lwjglLibs.forEach(::api)

	api(projects.platform)
	api(projects.graphicsCommon)
	implementation(projects.core)

	testImplementation(libs.bundles.test)
}
