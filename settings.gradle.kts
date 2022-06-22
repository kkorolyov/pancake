val lwjglVariants = listOf("natives-linux", "natives-windows", "natives-macos")
val jfxVariants = listOf("linux", "win", "mac")

rootProject.name = "pancake"

include("platform")
include("core")

multiPlatform("audio-jfx", jfxVariants)
multiPlatform("audio-al", lwjglVariants)

include("graphics-common")
multiPlatform("graphics-jfx", jfxVariants)
multiPlatform("graphics-gl", lwjglVariants)

include("input-common")
multiPlatform("input-jfx", jfxVariants)
multiPlatform("input-glfw", lwjglVariants)

multiPlatform("editor", jfxVariants)
multiPlatform("editor-core", jfxVariants)

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
