# How to add as a Gradle dependency

`gradle/libs.versions.toml`

```toml
[versions]
pancake = "<pancake-version>"

[libraries]
pancake-platform = { module = "dev.kkorolyov.pancake:platform", version.ref = "pancake" }
```

`build.gradle(.kts)`

```kotlin
dependencies {
	implementation(libs.pancake.platform)
}
```
