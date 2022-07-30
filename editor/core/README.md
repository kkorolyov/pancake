# editor-core

Adds widgets for components and systems provided by [core](../../core/README.md).

---

# Usage

This module depends on [editor](../README.md) and provides implementations for [`ComponentWidgetFactory`](../src/main/kotlin/dev/kkorolyov/pancake/editor/ComponentWidgetFactory.kt) following the `ServiceLoader` pattern.

## Gradle

### Inline

`build.gradle(.kts)`

```kotlin
dependencies {
	implementation("dev.kkorolyov.pancake:editor-core:<pancake-version>")
}
```

### Version catalog

`gradle/libs.versions.toml`

```toml
[versions]
pancake = "<pancake-version>"

[libraries]
pancake-editor-core = { module = "dev.kkorolyov.pancake:editor-core", version.ref = "pancake" }
```

`build.gradle(.kts)`

```kotlin
dependencies {
	implementation(libs.pancake.editor.core)
}
```
