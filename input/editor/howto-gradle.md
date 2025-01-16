# How to add as a Gradle dependency

This module depends on [editor](../../editor/howto-gradle.md) and provides implementations for `Component`-type [`WidgetFactory`](../../editor/src/main/kotlin/dev/kkorolyov/pancake/editor/factory/WidgetFactory.kt) following the `ServiceLoader` pattern.

`gradle/libs.versions.toml`

```toml
[versions]
pancake = "<pancake-version>"

[libraries]
pancake-core-editor = { module = "dev.kkorolyov.pancake:input-editor", version.ref = "pancake" }
```

`build.gradle(.kts)`

```kotlin
dependencies {
	implementation(libs.pancake.input.editor)
}
```
