[![Build](https://github.com/kkorolyov/pancake/workflows/build/badge.svg)](https://github.com/kkorolyov/pancake/actions/workflows/build.yaml)

# ![Pancake](docs/pancake.png)

A modular, lightweight, and extensible entity-component system (ECS) game engine for the JVM.

## Modules

* [platform](platform/README.md) - base constructs for crafting and running a Pancake application
* [core](core/README.md) - common, reusable system + component implementations
* [editor](editor/README.md) - realtime Pancake application editor
* [audio](audio/README.md) - audio-oriented system + component implementations
* [graphics](graphics/README.md) - graphics rendering system + component implementations
* [input](input/README.md) - input and control system + component implementations

## Committing and Publishing

PRs tagged with the `major`, `minor`, or `patch` labels will publish a new release on merge to `master`.
