rootProject.name = "pancake"

include("platform")
include("core")

include("demo:wasdbox")

include("test-utils")

include("plugin:apprender:jfx")
include("plugin:audio:jfx")
include("graphics:jfx")

include("killstreek")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
