plugins {
	`java-library`
}
apply(from = "$rootDir/kotlin.gradle")
apply(from = "$rootDir/publish.gradle.kts")

description = "Provides debug fragments for Core components"

dependencies {
	implementation(projects.platform)
	implementation(projects.core)
	implementation(projects.editor)

	testImplementation(testFixtures(projects.editor))
	testImplementation(testFixtures(projects.platform))

	dependencyLocking {
		ignoredDependencies.add("io.github.spair:imgui-java-natives*")
	}
}

(extra["setupLwjgl"] as (Any) -> Unit)(listOf(libs.lwjgl.opengl, libs.lwjgl.glfw, libs.lwjgl.stb))

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
			"""import (.*\w+?ActionWidgetFactory)""".toRegex().findAll(moduleInfoText)
				.map { it.groups[1] }
				.joinToString("\n")
		)
		componentWidgetFactoryServicesFile.writeText(
			"""import (.*\w+?ComponentWidgetFactory)""".toRegex().findAll(moduleInfoText)
				.map { it.groupValues[1] }
				.joinToString("\n")
		)
		gameSystemWidgetFactoryServicesFile.writeText(
			"""import (.*\w+?SystemWidgetFactory)""".toRegex().findAll(moduleInfoText)
				.map { it.groupValues[1] }
				.joinToString("\n")
		)
	}
}

tasks.register<JavaExec>("e2e") {
	group = "verification"
	dependsOn("generateTestResources")

	mainClass.set("dev.kkorolyov.pancake.editor.core.test.e2e.E2EKt")
	classpath = sourceSets.test.get().runtimeClasspath

	doLast {
		println(sourceSets.test.get().output.map { it.path })
	}
}
