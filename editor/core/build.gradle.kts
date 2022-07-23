plugins {
	`java-library`
}
apply(from = "$rootDir/kotlin.gradle")
apply(from = "$rootDir/publish.gradle.kts")

description = "Provides debug fragments for Core components"

dependencies {
	implementation(libs.bundles.stdlib)

	implementation(projects.platform)
	implementation(projects.core)
	implementation(projects.editor)

	testImplementation(testFixtures(projects.editor))

	dependencyLocking {
		ignoredDependencies.add("io.github.spair:imgui-java-natives*")
	}
}

(extra["setupLwjgl"] as (Any) -> Unit)(listOf(libs.lwjgl.opengl, libs.lwjgl.glfw, libs.lwjgl.stb))

tasks.register<JavaExec>("e2e") {
	group = "verification"

	mainClass.set("e2e.E2EKt")
	classpath = sourceSets.test.get().runtimeClasspath
}
