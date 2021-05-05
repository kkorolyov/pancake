plugins {
	id("common")
	id("kt")
	id("jfx")
	id("testable")
	application
}

description = "Top-down ARPG with dynamic RNG system"

dependencies {
	val log4jVersion: String by project
	implementation("org.apache.logging.log4j:log4j-slf4j18-impl:$log4jVersion")
	val jacksonVersion: String by project
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")

	implementation(projects.platform)
	implementation(projects.core)

	runtimeOnly(projects.plugin.appRender.jfx)
	runtimeOnly(projects.plugin.audio.jfx)

	testImplementation(projects.testUtils)
}

tasks.compileJava {
	options.compilerArgs.addAll(
		listOf(
			"--patch-module", "dev.kkorolyov.killstreek=${sourceSets.main.get().output.asPath}"
		)
	)
}

application {
	mainModule.set("dev.kkorolyov.killstreek")
	mainClass.set("dev.kkorolyov.killstreek.LauncherKt")
}

tasks.named<JavaExec>("run") {
	dependsOn("installDist")

	// Launch alongside loose resources
	workingDir = tasks.named<Sync>("installDist").get().destinationDir
}
