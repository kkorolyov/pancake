# audio-al

Audio components and systems implemented using `OpenAL`.

---

# Usage

This module requires [`lwjgl`](https://github.com/LWJGL/lwjgl3) native binaries to be on the classpath: [`lwjgl`, `lwjgl-openal`].
It is not necessary to provide a version specifier for the `lwjgl` dependencies.

## Gradle

### Inline

`build.gradle(.kts)`

```kotlin
dependencies {
	implementation("dev.kkorolyov.pancake:audio-al:<pancake-version>")

	val os = org.gradle.internal.os.OperatingSystem.current()
	val lwjglPlatform = "natives-${if (os.isWindows) "windows" else if (os.isMacOsX) "macos" else "linux"}"
	implementation("org.lwjgl:lwjgl::$lwjglPlatform")
	implementation("org.lwjgl:lwjgl-openal::$lwjglPlatform")
}
```

### Version catalog

`gradle/libs.versions.toml`

```toml
[versions]
pancake = "<pancake-version>"

[libraries]
pancake-audio-al = { module = "dev.kkorolyov.pancake:audio-al", version.ref = "pancake" }

lwjgl = { module = "org.lwjgl:lwjgl" }
lwjgl-openal = { module = "org.lwjgl:lwjgl-openal" }

[bundles]
lwjgl = ["lwjgl", "lwjgl-openal"]
```

`build.gradle(.kts)`

```kotlin
dependencies {
	implementation(libs.pancake.audio.al)

	val os = org.gradle.internal.os.OperatingSystem.current()
	val lwjglPlatform = "natives-${if (os.isWindows) "windows" else if (os.isMacOsX) "macos" else "linux"}"
	libs.bundles.lwjgl.get()
		.map { it.module }
		.forEach {
			implementation("${it.group}:${it.name}::$lwjglPlatform")
		}
}
```
