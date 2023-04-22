plugins {
	configKotlin
	configLwjgl
	configPublish
}

description = "Provides debug fragments for Core components"

dependencies {
	implementation(projects.platform)
	implementation(projects.core)
	implementation(projects.editor)

	implementation(libs.classgraph)

	testImplementation(testFixtures(projects.platform))
	testImplementation(testFixtures(projects.editor))

	dependencyLocking {
		ignoredDependencies.add("io.github.spair:imgui-java-natives*")
	}
}

val setupLwjgl: (Any) -> Unit by extra
setupLwjgl(listOf(libs.lwjgl.opengl, libs.lwjgl.glfw, libs.lwjgl.stb))

tasks.register("generateTestResources") {
	val componentConverterServicesFile = file("$buildDir/resources/test/META-INF/services/dev.kkorolyov.pancake.platform.entity.ComponentConverter")
	val widgetFactoryServicesFile = file("$buildDir/resources/test/META-INF/services/dev.kkorolyov.pancake.editor.factory.WidgetFactory")

	outputs.files(
		componentConverterServicesFile,
		widgetFactoryServicesFile
	)

	doLast {
		val componentConverterModuleInfoText = file("${project(":core").projectDir}/src/main/java/module-info.java").readText()
		val widgetFactoryModuleInfoText = file("$projectDir/src/main/java/module-info.java").readText()

		componentConverterServicesFile.writeText(
			"""(?<=import\s)[\w.]*\w+ComponentConverter""".toRegex().findAll(componentConverterModuleInfoText)
				.map(MatchResult::value)
				.joinToString("\n")
		)
		widgetFactoryServicesFile.writeText(
			"""(?<=import\s)[\w.]*\w+(Action|Component|System)WidgetFactory""".toRegex().findAll(widgetFactoryModuleInfoText)
				.map(MatchResult::value)
				.joinToString("\n")
		)
	}
}

tasks.register<JavaExec>("e2e") {
	group = "verification"
	dependsOn("generateTestResources")

	mainClass.set("dev.kkorolyov.pancake.core.editor.test.e2e.E2EKt")
	classpath = sourceSets.test.get().runtimeClasspath
}
