plugins {
	id("configBase")
	kotlin("jvm")
	id("org.jetbrains.dokka")
}

tasks.compileJava {
	val file = file("$projectDir/src/main/java/module-info.java")

	if (file.exists()) {
		val moduleName = """(?<=module\s)[\w.]+""".toRegex().find(file("$projectDir/src/main/java/module-info.java").readText())!!.value

		inputs.property("moduleName", moduleName)
		options.compilerArgs = listOf(
			"--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
		)
	}
}
