plugins {
	`java-library`
	groovy
}
apply(from = "../kotlin.gradle")
apply(from = "../publish.gradle.kts")
apply(from = "../lwjgl.gradle.kts")

description = "GLFW input system and control implementations"

val lwjglLibs = (extra["lwjglExpand"] as (Any) -> List<Any>)(libs.lwjgl.run { listOf(asProvider(), glfw) })
dependencies {
	implementation(libs.bundles.stdlib)

	lwjglLibs.forEach(::api)

	api(projects.platform)
	api(projects.inputCommon)
	implementation(projects.core)

	testImplementation(libs.bundles.test)
}
