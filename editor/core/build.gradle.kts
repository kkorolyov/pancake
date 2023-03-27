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

	testImplementation(testFixtures(projects.editor))
	testImplementation(testFixtures(projects.platform))

	dependencyLocking {
		ignoredDependencies.add("io.github.spair:imgui-java-natives*")
	}
}

val setupLwjgl: (Any) -> Unit by extra
setupLwjgl(listOf(libs.lwjgl.opengl, libs.lwjgl.glfw, libs.lwjgl.stb))

tasks.register("generateTestResources") {
	val servicesDir = "$buildDir/resources/test/META-INF/services"
	val actionWidgetFactoryServicesFile = file("$servicesDir/dev.kkorolyov.pancake.editor.factory.ActionWidgetFactory")
	val componentWidgetFactoryServicesFile = file("$servicesDir/dev.kkorolyov.pancake.editor.factory.ComponentWidgetFactory")
	val gameSystemWidgetFactoryServicesFile = file("$servicesDir/dev.kkorolyov.pancake.editor.factory.GameSystemWidgetFactory")

	outputs.files(
		actionWidgetFactoryServicesFile,
		componentWidgetFactoryServicesFile,
		gameSystemWidgetFactoryServicesFile
	)

	doLast {
		val moduleInfoText = file("$projectDir/src/main/java/module-info.java").readText()

		actionWidgetFactoryServicesFile.writeText(
			"""(?<=import\s)[\w.]*\w+ActionWidgetFactory""".toRegex().findAll(moduleInfoText)
				.map(MatchResult::value)
				.joinToString("\n")
		)
		componentWidgetFactoryServicesFile.writeText(
			"""(?<=import\s)[\w.]*\w+ComponentWidgetFactory""".toRegex().findAll(moduleInfoText)
				.map(MatchResult::value)
				.joinToString("\n")
		)
		gameSystemWidgetFactoryServicesFile.writeText(
			"""(?<=import\s)[\w.]*\w+SystemWidgetFactory""".toRegex().findAll(moduleInfoText)
				.map(MatchResult::value)
				.joinToString("\n")
		)
	}
}

tasks.register<JavaExec>("e2e") {
	group = "verification"
	dependsOn("generateTestResources")

	mainClass.set("dev.kkorolyov.pancake.editor.core.test.e2e.E2EKt")
	classpath = sourceSets.test.get().runtimeClasspath
}
