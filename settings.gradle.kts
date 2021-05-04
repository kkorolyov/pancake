rootProject.name = "pancake"

include("platform")
include("core")

include("test-utils")

include("svc:app-render:jfx")
include("svc:audio:jfx")

include("killstreek")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
