rootProject.name = "pancake"

include("platform")
include("core")

include("editor")
includeIn("editor-core", "editor/core")

includeIn("audio-al", "audio/al")

include("graphics")
includeIn("graphics-gl", "graphics/gl")

include("input")
includeIn("input-glfw", "input/glfw")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

fun includeIn(id: String, path: String) {
	include(id)
	project(":$id").projectDir = file(path)
}
