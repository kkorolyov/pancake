plugins {
	configKotlin
	configLwjgl
	configPublish
}

description = "OpenGL rendering system and renderable implementations"

dependencies {
	api(projects.platform)
	api(projects.graphics)
	implementation(projects.core)

	testImplementation(libs.lwjgl.glfw)
	val lwjglNatives = if (org.gradle.internal.os.OperatingSystem.current().isWindows) "natives-windows" else if (org.gradle.internal.os.OperatingSystem.current().isMacOsX) "natives-macos" else "natives-linux"
	listOf(libs.lwjgl.asProvider(), libs.lwjgl.glfw, libs.lwjgl.opengl, libs.lwjgl.stb).forEach {
		testImplementation(variantOf(it) { classifier(lwjglNatives) })
	}

	dependencyLocking {
		ignoredDependencies.add("org.lwjgl:lwjgl-glfw")
	}
}

val setupLwjgl: (Any) -> Unit by extra
setupLwjgl(listOf(libs.lwjgl.opengl, libs.lwjgl.stb))

tasks.register<JavaExec>("e2e") {
	group = "verification"

	mainClass.set("dev.kkorolyov.pancake.graphics.gl.test.e2e.E2EKt")
	classpath = sourceSets.test.get().runtimeClasspath
}
