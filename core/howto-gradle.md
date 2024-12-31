# How to add as a Gradle dependency

`gradle/libs.versions.toml`

```toml
[versions]
pancake = "<pancake-version>"

[libraries]
pancake-core = { module = "dev.kkorolyov.pancake:core", version.ref = "pancake" }
```

`build.gradle(.kts)`

```kotlin
dependencies {
	implementation(libs.pancake.core)
}
```
