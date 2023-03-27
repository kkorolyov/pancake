plugins {
	id("configBase")
}

fun libs(name: String) = the<VersionCatalogsExtension>().named("libs").findLibrary(name).get()

val setupLwjgl by extra({ deps: List<Provider<MinimalExternalModuleDependency>> ->
	dependencies {
		val api = configurations["api"]

		val allDeps = deps + libs("lwjgl")

		api(platform(libs("lwjgl-bom")))
		allDeps.forEach { api(it) }
	}
})
