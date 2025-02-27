rootProject.name = "pancake"

include("platform")

include("core")
includeIn("core-editor", "core/editor")

include("editor")

includeIn("audio-al", "audio/al")

include("graphics")
includeIn("graphics-gl", "graphics/gl")
includeIn("graphics-editor", "graphics/editor")

include("input")
includeIn("input-editor", "input/editor")
includeIn("input-glfw", "input/glfw")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

fun includeIn(id: String, path: String) {
	include(id)
	project(":$id").projectDir = file(path)
}
