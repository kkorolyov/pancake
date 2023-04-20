# editor

Realtime Pancake application editor widgets library.

---

# Usage

This module requires [`lwjgl`](https://github.com/LWJGL/lwjgl3) and [`imgui-java`](https://github.com/SpaiR/imgui-java) native binaries to be on the classpath: [`lwjgl`, `lwjgl-glfw`, `lwjgl-opengl`, `lwjgl-stb`, `imgui-<platform>`].
It is not necessary to provide a version specifier for the `lwjgl` dependencies - it is assumed the version will be provided by `lwjgl`-enabled `pancake` dependencies.

## Gradle

### Inline

`build.gradle(.kts)`

```kotlin
dependencies {
	implementation("dev.kkorolyov.pancake:editor:<pancake-version>")

	val os = org.gradle.internal.os.OperatingSystem.current()
	val platform = if (os.isWindows) "windows" else if (os.isMacOsX) "macos" else "linux"

	implementation("org.lwjgl:lwjgl::natives-$platform")
	implementation("org.lwjgl:lwjgl-glfw::natives-$platform")
	implementation("org.lwjgl:lwjgl-opengl::natives-$platform")
	implementation("org.lwjgl:lwjgl-stb::natives-$platform")

	implementation("io.github.spair:imgui-java-natives-$platform:1.+")
}
```

### Version catalog

`gradle/libs.versions.toml`

```toml
[versions]
pancake = "<pancake-version>"

imgui = "1.+"

[libraries]
pancake-editor = { module = "dev.kkorolyov.pancake:editor", version.ref = "pancake" }

lwjgl = { module = "org.lwjgl:lwjgl" }
lwjgl-glfw = { module = "org.lwjgl:lwjgl-glfw" }
lwjgl-opengl = { module = "org.lwjgl:lwjgl-opengl" }
lwjgl-stb = { module = "org.lwjgl:lwjgl-stb" }

imgui-windows = { module = "io.github.spair:imgui-java-natives-windows", version.ref = "imgui" }
imgui-macos = { module = "io.github.spair:imgui-java-natives-macos", version.ref = "imgui" }
imgui-linux = { module = "io.github.spair:imgui-java-natives-linux", version.ref = "imgui" }

[bundles]
lwjgl = ["lwjgl", "lwjgl-glfw", "lwjgl-opengl", "lwjgl-stb"]
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

	implementation(
		if (os.isWindows) libs.imgui.windows
		else if (os.isMacOsX) libs.imgui.macos
		else libs.imgui.linux
	)
}
```
