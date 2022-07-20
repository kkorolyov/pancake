rootProject.name = "pancake"

include("platform")
include("core")

include("audio-al")

include("graphics-common")
include("graphics-gl")

include("input-common")
include("input-glfw")

include("editor")
include("editor-core")

include("test-utils")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
