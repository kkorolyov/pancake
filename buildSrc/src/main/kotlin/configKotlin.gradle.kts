plugins {
	id("configBase")
	kotlin("jvm")
	id("org.jetbrains.dokka")
}

tasks.compileKotlin {
	destinationDirectory.set(tasks.compileJava.get().destinationDirectory)
}
