val platforms = listOf("natives-linux", "natives-windows", "natives-macos")
extra["lwjglExpand"] = { deps: List<Provider<MinimalExternalModuleDependency>> -> listOf(dependencies.platform(libs.lwjgl.bom), *deps.flatMap { dep -> platforms.map { platform -> dependencies.variantOf(dep) { classifier(platform) } } }.toTypedArray(), *deps.toTypedArray()) }
