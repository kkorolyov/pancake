plugins {
	`kotlin-dsl`
	idea
}

repositories {
	gradlePluginPortal()
}
dependencies {
	implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")
	implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.+")
	implementation("com.dua3.gradle:javafx-plugin:0.+")
}
