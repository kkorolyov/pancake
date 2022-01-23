rootProject.name = "pancake"

include("platform")
include("core")

include("audio-jfx")
include("graphics-jfx")
include("input-jfx")

include("demo:wasdbox")
include("demo:wiggles")
include("demo-bounce")

include("debug")
include("debug-core")

include("test-utils")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")
