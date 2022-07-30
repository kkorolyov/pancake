# input-glfw

Input and control components and systems implemented using `GLFW`.

---

# Usage

This module requires [`lwjgl`](https://github.com/LWJGL/lwjgl3) native binaries to be on the classpath: [`lwjgl`, `lwjgl-glfw`].
It is not necessary to provide a version specifier for the `lwjgl` dependencies.

## Gradle

### Inline

`build.gradle(.kts)`

```kotlin
dependencies {
	implementation("dev.kkorolyov.pancake:input-glfw:<pancake-version>")

	val os = org.gradle.internal.os.OperatingSystem.current()
	val lwjglPlatform = "natives-${if (os.isWindows) "windows" else if (os.isMacOsX) "macos" else "linux"}"
	implementation("org.lwjgl:lwjgl::$lwjglPlatform")
	implementation("org.lwjgl:lwjgl-glfw::$lwjglPlatform")
}
```

### Version catalog

`gradle/libs.versions.toml`

```toml
[versions]
pancake = "<pancake-version>"

[libraries]
pancake-input-glfw = { module = "dev.kkorolyov.pancake:input-glfw", version.ref = "pancake" }

lwjgl = { module = "org.lwjgl:lwjgl" }
lwjgl-glfw = { module = "org.lwjgl:lwjgl-glfw" }

[bundles]
lwjgl = ["lwjgl", "lwjgl-glfw"]
```

`build.gradle(.kts)`

```kotlin
dependencies {
	implementation(libs.pancake.input.glfw)

	val os = org.gradle.internal.os.OperatingSystem.current()
	val lwjglPlatform = "natives-${if (os.isWindows) "windows" else if (os.isMacOsX) "macos" else "linux"}"
	libs.bundles.lwjgl.get()
		.map { it.module }
		.forEach {
			implementation("${it.group}:${it.name}::$lwjglPlatform")
		}
}
```
