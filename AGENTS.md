# Pancake ECS Game Engine — Context & Conventions

## What is this repo?

Pancake is a lightweight, modular ECS (Entity-Component-System) game engine for the JVM. It follows the ECS pattern: `GameEngine` runs ordered `Pipeline`s (dynamic or fixed timestep), each containing `GameSystem`s that process `Entity`s with matching `Component` signatures.

## Module structure

Base package: `dev.kkorolyov.pancake`

**12 modules** (14 project names due to path-based naming):

| Project name | File path | Language | Purpose |
|---|---|---|---|
| `platform` | `platform/` | Java | Core ECS constructs: `GameEngine`, `Pipeline`, `GameSystem`, `EntityPool`, `Entity`, `Component` |
| `core` | `core/` | Java | Common reusable components & systems (Movement, Collisions, Action/Chain/Spawn) |
| `audio` | `audio/` | — | Parent module (no source). Mutually-exclusive submodules for audio backends. |
| `audio-al` | `audio/al/` | Kotlin | OpenAL audio components (`AudioEmitter`, `AudioReceiver`) and systems |
| `graphics` | `graphics/` | Kotlin | Implementation-agnostic rendering abstractions (`RenderBackend`, `Camera`, `Atlas`, `Font`) |
| `graphics-gl` | `graphics/gl/` | Kotlin | OpenGL rendering implementation (`GLRenderBackend`) |
| `input` | `input/` | Kotlin | Input abstraction layer (events, controls) |
| `input-glfw` | `input/glfw/` | Kotlin | GLFW input implementation |
| `editor` | `editor/` | Kotlin | ImGui-based realtime Pancake application editor widgets library |
| `core-editor` | `core/editor/` | Kotlin | Editor widgets for core components (`ServiceLoader`-based `WidgetFactory`) |
| `graphics-editor` | `graphics/editor/` | Kotlin | Editor widgets for graphics components |
| `input-editor` | `input/editor/` | Kotlin | Editor widgets for input components |

### Dependency graph

```
platform
  └── core
        ├── audio-al
        ├── graphics
        │     └── graphics-gl
        ├── input
        │     └── input-glfw
        └── editor
              ├── core-editor
              ├── graphics-editor
              └── input-editor
```

### Naming conventions

- Module names use `-` (kebab-case): `audio-al`, `graphics-gl`, `input-glfw`
- Project names map to filesystem paths via `includeIn()` in `settings.gradle.kts`
- All modules share group: `dev.kkorolyov.pancake`

## Language and architecture

- **Java** modules: `platform`, `core` — core ECS constructs are Java for maximum compatibility and zero Kotlin dependency
- **Kotlin** modules: `editor`, `graphics`, `audio/al`, `graphics/gl`, `input`, `input/glfw`, and all `*-editor` submodules
- All modules use **Java 9+ modules** (`module-info.java`) with `requires`/`exports`/`provides` declarations
- Kotlin modules patch `module-info.java` via `--patch-module` in `buildSrc/src/main/kotlin/configKotlin.gradle.kts`

## Build system

- **Gradle** with Kotlin DSL (`*.gradle.kts`)
- **Version catalog**: `gradle/libs.versions.toml` — manages all dependency versions
- **Convention plugins** in `buildSrc/src/main/kotlin/`:
  - `configBase` — base plugin: maven repos, dependency locking, JUnit platform, compile/implementation/test/testRuntime from catalog
  - `configKotlin` — applies `configBase` + Kotlin JVM + module-info patching
  - `configPublish` — applies `configBase` + java-library + maven-publish + GH Packages publishing
  - `configLwjgl` — applies `configBase`, provides `setupLwjgl` extra for LWJGL BOM
- **Dependency locking**: enabled on all modules (`settings-gradle.lockfile`)
- **Test frameworks**: Spock (Groovy) for most modules; JUnit 5 + ByteBuddy for runtime; Kotlin for e2e tests
- **Build command**: `./gradlew build` (Java 21, Temurin)

## Key patterns

### ServiceLoader-based extensibility

- **Serializers**: `platform` declares `uses Serializer`; implementations `provides` via `ServiceLoader` (JSON/YAML via Jackson)
- **Editor widgets**: `*-editor` modules implement `WidgetFactory` interfaces, registered via `ServiceLoader`

### Platform detection

Native library classifiers (windows/macos/linux) are selected at build time for LWJGL and OpenAL.

## CI/CD

- **build.yaml**: Runs `./gradlew build` on push to `master` and on PRs. Uploads reports on failure.
- **bump.yaml**: On push to `master`, runs `./gradlew allDeps --write-locks`. If lockfile changed, opens a PR via `peter-evans/create-pull-request`.
- **release.yaml**: Triggered on PR close (merged). Label (`major`/`minor`/`patch`) determines version bump. Creates GitHub release + publishes artifacts.
- **dependabot**: Daily updates for GitHub Actions only.

## File locations for quick reference

- Version catalog: `gradle/libs.versions.toml`
- Convention plugins: `buildSrc/src/main/kotlin/config*.gradle.kts`
- Module READMEs: `platform/README.md`, `core/README.md`, `audio/README.md`, `audio/al/README.md`, `graphics/README.md`, `graphics/gl/README.md`, `input/README.md`, `input/glfw/README.md`, `editor/README.md`, `core/editor/README.md`, `graphics/editor/README.md`, `input/editor/README.md`
- Architecture diagrams: `platform/reference.md` (Mermaid diagrams for GameEngine, Pipeline, GameSystem loops)
- Editor reference: `editor/howto-gradle.md`
- All modules have `howto-gradle.md` with module-specific build instructions

## Coding conventions

- **Kotlin code style**: `official` (set in `gradle.properties`)
- **EditorConfig**: LF line endings, final newline, 2-space indent (tabs for code), 2-space indent for YAML
- **No ktlint/detekt/spotless** configured — rely on Kotlin official style + EditorConfig
- **Module naming**: kebab-case for project names (`audio-al`, `graphics-gl`)

## Testing

- Spock (`org.spockframework:spock`) + JUnit 5 (`org.junit.jupiter`) + ByteBuddy (`net.bytebuddy`)
- Tests live in `src/test/groovy/` (Spock specs) or `src/test/kotlin/` (e2e)
- Test fixtures used in `platform`, `editor`, `core/editor`, `graphics`
- Resources in `src/test/resources/` (shaders, images, fonts, config)
