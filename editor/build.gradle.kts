import org.gradle.internal.os.OperatingSystem

plugins {
	`java-library`
}
apply(from = "$rootDir/kotlin.gradle")
apply(from = "$rootDir/publish.gradle.kts")

description = "Graphical debugging tools that can hook into a Pancake application"

dependencies {
	implementation(libs.bundles.stdlib)

	api(libs.imgui.binding)
	implementation(libs.imgui.lwjgl)

	implementation(projects.platform)

	val lwjglNatives = if (OperatingSystem.current().isWindows) "natives-windows" else if (OperatingSystem.current().isMacOsX) "natives-macos" else "natives-linux"
	listOf(libs.lwjgl.asProvider(), libs.lwjgl.glfw, libs.lwjgl.opengl, libs.lwjgl.stb).forEach {
		testImplementation(variantOf(it) { classifier(lwjglNatives) })
	}
	testImplementation(libs.imgui.run { if (OperatingSystem.current().isWindows) windows else if (OperatingSystem.current().isMacOsX) macos else linux })
}

(extra["setupLwjgl"] as (Any) -> Unit)(listOf(libs.lwjgl.opengl, libs.lwjgl.glfw, libs.lwjgl.stb))
