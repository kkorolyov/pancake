[![Build](https://github.com/kkorolyov/pancake/workflows/build/badge.svg)](https://github.com/kkorolyov/pancake/actions/workflows/build.yaml)

# ![Pancake](docs/pancake.png)

A modular, lightweight, and extensible entity-component system (ECS)-driven game engine for the JVM.

## Modules

* [platform](platform/README.md) - base constructs for crafting and running a Pancake application
* [core](core/README.md) - common, reusable system + component implementations
* [editor](editor/README.md) - realtime Pancake application editor
* [audio](audio/README.md) - audio-oriented system + component implementations
* [graphics](graphics/README.md) - graphics rendering system + component implementations
* [input](input/README.md) - input and control system + component implementations

## Committing and Publishing

Commits to `master` with `MAJOR`, `MINOR`, or `PATCH` keywords in the commit message will publish bumped artifact versions from the build of that commit.
