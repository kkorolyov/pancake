import org.gradle.internal.os.OperatingSystem

plugins {
	configKotlin
	configLwjgl
	configPublish
	`java-test-fixtures`
}

description = "Graphical debugging tools that can hook into a Pancake application"

dependencies {
	api(libs.imgui.binding)
	implementation(libs.imgui.lwjgl)

	implementation(projects.platform)
	api(projects.graphics)

	implementation(libs.classgraph)
	implementation(libs.snakeyaml)

	testFixturesImplementation(projects.platform)
	val lwjglNatives = if (OperatingSystem.current().isWindows) "natives-windows" else if (OperatingSystem.current().isMacOsX) "natives-macos" else "natives-linux"
	listOf(libs.lwjgl.asProvider(), libs.lwjgl.glfw, libs.lwjgl.opengl, libs.lwjgl.stb).forEach {
		testFixturesImplementation(variantOf(it) { classifier(lwjglNatives) })
	}
	testFixturesImplementation(libs.imgui.run { if (OperatingSystem.current().isWindows) windows else if (OperatingSystem.current().isMacOsX) macos else linux })

	dependencyLocking {
		ignoredDependencies.add("io.github.spair:imgui-java-natives*")
	}
}

val setupLwjgl: (Any) -> Unit by extra
setupLwjgl(listOf(libs.lwjgl.opengl, libs.lwjgl.glfw, libs.lwjgl.stb))

tasks.register<JavaExec>("e2e") {
	group = "verification"

	mainClass.set("dev.kkorolyov.pancake.editor.test.e2e.E2EKt")
	classpath = sourceSets.test.get().runtimeClasspath
}
