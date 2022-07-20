val jfxVariants = listOf("linux", "win", "mac")

rootProject.name = "pancake"

include("platform")
include("core")

multiPlatform("audio-jfx", jfxVariants)
include("audio-al")

include("graphics-common")
multiPlatform("graphics-jfx", jfxVariants)
include("graphics-gl")

include("input-common")
multiPlatform("input-jfx", jfxVariants)
include("input-glfw")

include("editor")
include("editor-core")

include("test-utils")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

fun multiPlatform(root: String, variants: List<String>) {
	include(root)

	variants.forEach {
		include("$root:$it")

		val dir = File("$root/$it")
		if (!dir.exists()) dir.mkdirs()
	}
}
