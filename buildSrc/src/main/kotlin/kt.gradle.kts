import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	id("org.jetbrains.dokka")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "14"
	}
}
tasks.compileJava {
	options.compilerArgs.addAll(
		listOf(
			// include kotlin output on modulepath
			"--patch-module", "${project.group}.${project.name}=${sourceSets.main.get().output.asPath}"
		)
	)
}

tasks.withType<Javadoc> {
	enabled = false
}
