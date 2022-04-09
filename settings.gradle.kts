rootProject.name = "pancake"

include("platform")
include("core")

include("audio-jfx")
multiPlatform("audio-al")

include("graphics-common")
include("graphics-jfx")
multiPlatform("graphics-gl")

include("input-common")
include("input-jfx")
multiPlatform("input-glfw")

include("editor")
include("editor-core")

include("test-utils")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

fun multiPlatform(root: String) {
	include(root)

	listOf("linux", "windows", "macos").forEach {
		include("$root:$it")

		val dir = File("$root/$it")
		if (!dir.exists()) dir.mkdirs()
	}
}
