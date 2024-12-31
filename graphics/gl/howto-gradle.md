# How to add as a Gradle dependency

This module requires [`lwjgl`](https://github.com/LWJGL/lwjgl3) native binaries to be on the classpath: [`lwjgl`, `lwjgl-opengl`, `lwjgl-stb`].
It is not necessary to provide a version specifier for the `lwjgl` dependencies.

`gradle/libs.versions.toml`

```toml
[versions]
pancake = "<pancake-version>"

[libraries]
pancake-graphics-gl = { module = "dev.kkorolyov.pancake:graphics-gl", version.ref = "pancake" }

lwjgl = { module = "org.lwjgl:lwjgl" }
lwjgl-opengl = { module = "org.lwjgl:lwjgl-opengl" }
lwjgl-stb = { module = "org.lwjgl:lwjgl-stb" }

[bundles]
lwjgl = ["lwjgl", "lwjgl-opengl", "lwjgl-stb"]
```

`build.gradle(.kts)`

```kotlin
dependencies {
	implementation(libs.pancake.graphics.gl)

	val os = org.gradle.internal.os.OperatingSystem.current()
	val lwjglPlatform = "natives-${if (os.isWindows) "windows" else if (os.isMacOsX) "macos" else "linux"}"
	libs.bundles.lwjgl.get()
		.map { it.module }
		.forEach {
			implementation("${it.group}:${it.name}::$lwjglPlatform")
		}
}
```
