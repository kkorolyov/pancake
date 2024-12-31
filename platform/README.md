# platform

The bare-bones, most basic, foundational base of the engine.

Provides a few specialized, fundamental constructs:

| Construct    | Function                                                                |
|--------------|-------------------------------------------------------------------------|
| `GameEngine` | Runs the simulation loop                                                |
| `Pipelin`    | Groups related systems into an atomically-updated unit                  |
| `GameSystem` | Executes specialized logic given relevant entities and their components |
| `EntityPool` | Creates, destroys, and returns entities                                 |
| `Entity`     | Provides a uniquely identifiable, mutable collection of components      |
| `Component`  | Contains specialized data                                               |

The [reference](reference.md#architecture) provides an architecture overview.

## The engine

A `GameEngine` instance serves as the heart of simulation flow and is instantiated with an ordered list of `Pipeline`s to execute.

Once started, an engine continuously loops on the current thread and for each cycle, updates each of its provided `Pipeline`s once.
In each update, the pipeline is provided the number of nanoseconds elapsed (referred to as delta time, or `dt`) since the start of the previous cycle multiplied by the engine's speed factor.

Engine speed defaults to `1`, where simulation time is 1:1 with real time.
`< 1` gives a slower simulation time, whereas `> 1` gives a faster.
Speed can also be negative, which can be used to emulate rewind behavior in applicable game systems.

The [reference](reference.md#gameengine-loop) provides a timeline diagram of this.

## Pipelines

A `Pipeline` aggregates the set of `GameSystem`s that should be updated as a unit each cycle.

Pipelines update in 2 flavors: dynamic timestep or fixed timestep.

A dynamic pipeline updates its systems once with the provided `dt` each time it is invoked.

A fixed pipeline updates its systems using a consistent timestep value.
The pipeline accumulates update `dt` over successive update cycles and updates its systems (with the fixed timestep value) only once it has met or exceeded the minimum timestep.
Alternatively, if accumulated `dt` is far greater than the fixed interval - say in the event of a noticeable lag in the simulation - the pipeline may run its update cycle multiple times with the same fixed timestep to catch up.

The [reference](reference.md#pipeline-loop) provides a timeline diagram for both timestep flavors.

## Game systems

The platform provides an abstract `GameSystem` class intended to house the majority of custom implementation.

The [reference](reference.md#gamesystem-loop) provides a timeline diagram of system behavior.

## Entities

A `GameEngine` creates its own unique `EntityPool` which is shared with all attached `GameSystem`s.
The `EntityPool` supports creation, destruction, and locating of entities through `Component` signature.

## Resources

The [`Resources`](src/main/java/dev/kkorolyov/pancake/platform/io/Resources.java) class provides consistent mechanisms to opening streams to files.

`inStream` first tries to locate the file at the given path on the filesystem.
If not found, it tries to locate the file on the classpath.
If that also does not exist, it returns a `null` stream.
The rationale being that a Pancake application can ship with embedded default resources that can be replaced by the end-user without needing to repackage the entire application.

`outStream` simply opens an output stream to the given path on the filesystem.

---

# See also

- How to
	- [Use with Gradle](howto-gradle.md)
- [Reference](reference.md)
