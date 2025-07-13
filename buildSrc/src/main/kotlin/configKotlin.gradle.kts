plugins {
	id("configBase")
	kotlin("jvm")
}

tasks.compileJava {
	val moduleInfoFile = file("$projectDir/src/main/java/module-info.java")

	if (moduleInfoFile.exists()) {
		val moduleName = """(?<=module\s)[\w.]+""".toRegex().find(moduleInfoFile.readText())!!.value
		options.compilerArgs = listOf(
			"--patch-module", "$moduleName=${sourceSets["main"].output.asPath}"
		)
	}
}
