rootProject.name = "pancake"

include("platform")
include("core")

include("audio-jfx")

include("graphics-common")
include("graphics-jfx")

include("graphics-gl")
listOf("linux", "windows", "macos").forEach {
	val root = "graphics-gl"
	include("$root:$it")

	val dir = File("$root/$it")
	if (!dir.exists()) dir.mkdirs()
}

include("input-jfx")

include("editor")
include("editor-core")

include("test-utils")

include("gl-test")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
