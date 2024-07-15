rootProject.name = "pancake"

include("platform")

include("core")
includeIn("core-editor", "core/editor")

include("editor")

includeIn("audio-al", "audio/al")

include("graphics")
includeIn("graphics-gl", "graphics/gl")
includeIn("graphics-editor", "graphics/editor")
includeIn("graphics-gl-editor", "graphics/gl/editor")

include("input")
includeIn("input-glfw", "input/glfw")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

fun includeIn(id: String, path: String) {
	include(id)
	project(":$id").projectDir = file(path)
}

plugins {
	id("org.ajoberstar.reckon.settings") version "0.+"
}
extensions.configure<org.ajoberstar.reckon.gradle.ReckonExtension> {
	setDefaultInferredScope("patch")
	stages("rc", "final")
	setScopeCalc(calcScopeFromProp())
	setStageCalc(calcStageFromProp())
}
