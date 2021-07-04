rootProject.name = "pancake"

include("platform")
include("core")

include("audio:jfx")
include("graphics:jfx")
include("input:jfx")

include("demo:wasdbox")

include("test-utils")

// TODO Drop these
include("plugin:apprender:jfx")
include("plugin:audio:jfx")

include("killstreek")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
