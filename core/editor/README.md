# core-editor

Adds [editor](../../editor/README.md) widgets for components provided by [core](../../core/README.md).

---

# Usage

This module depends on [editor](../../editor/README.md) and provides implementations for `Component`-type [`WidgetFactory`](../../editor/src/main/kotlin/dev/kkorolyov/pancake/editor/factory/WidgetFactory.kt) following the `ServiceLoader` pattern.

## Gradle

### Inline

`build.gradle(.kts)`

```kotlin
dependencies {
	implementation("dev.kkorolyov.pancake:core-editor:<pancake-version>")
}
```

### Version catalog

`gradle/libs.versions.toml`

```toml
[versions]
pancake = "<pancake-version>"

[libraries]
pancake-core-editor = { module = "dev.kkorolyov.pancake:core-editor", version.ref = "pancake" }
```

`build.gradle(.kts)`

```kotlin
dependencies {
	implementation(libs.pancake.core.editor)
}
```
