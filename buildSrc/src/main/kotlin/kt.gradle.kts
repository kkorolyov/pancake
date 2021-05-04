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

tasks.withType<Javadoc> {
	enabled = false
}
