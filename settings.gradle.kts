rootProject.name = "pancake"

include("platform")
include("core")

include("audio-jfx")

include("graphics-common")
include("graphics-jfx")
withNested("graphics-gl", "linux", "windows", "macos")

include("input-common")
include("input-jfx")
withNested("input-glfw", "linux", "windows", "macos")

include("editor")
include("editor-core")

include("test-utils")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

fun withNested(root: String, vararg nested: String) {
	include(root)

	nested.forEach {
		include("$root:$it")

		val dir = File("$root/$it")
		if (!dir.exists()) dir.mkdirs()
	}
}
