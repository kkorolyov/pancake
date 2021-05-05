rootProject.name = "pancake"

include("platform")
include("core")

include("test-utils")

include("plugin:app-render:jfx")
include("plugin:audio:jfx")

include("killstreek")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
