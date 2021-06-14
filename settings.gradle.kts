rootProject.name = "pancake"

include("platform")
include("core")

include("graphics:jfx")
include("input:jfx")

include("demo:wasdbox")

include("test-utils")

include("plugin:apprender:jfx")
include("plugin:audio:jfx")

include("killstreek")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
