# core

Collection of common, reusable components and systems.

## Movement

[`AccelerationSystem`](src/main/java/dev/kkorolyov/pancake/core/system/AccelerationSystem.java) accelerates an entity's [`Velocity`](src/main/java/dev/kkorolyov/pancake/core/component/movement/Velocity.java) according to its [`Force`](src/main/java/dev/kkorolyov/pancake/core/component/movement/Force.java) and [`Mass`](src/main/java/dev/kkorolyov/pancake/core/component/movement/Mass.java).

[`CappingSystem`](src/main/java/dev/kkorolyov/pancake/core/system/CappingSystem.java) ensures an entity's [`Velocity`](src/main/java/dev/kkorolyov/pancake/core/component/movement/Velocity.java) does not exceed its [`VelocityCap`](src/main/java/dev/kkorolyov/pancake/core/component/movement/VelocityCap.java).

[`DampingSystem`](src/main/java/dev/kkorolyov/pancake/core/system/DampingSystem.java) diminishes an entity's [`Velocity`](src/main/java/dev/kkorolyov/pancake/core/component/movement/Velocity.java) proportionally according its [`Damping`](src/main/java/dev/kkorolyov/pancake/core/component/movement/Damping.java).

[`MovementSystem`](src/main/java/dev/kkorolyov/pancake/core/system/MovementSystem.java) translates an entity's [`Position`](src/main/java/dev/kkorolyov/pancake/core/component/Position.java) by its [`Velocity`](src/main/java/dev/kkorolyov/pancake/core/component/movement/Velocity.java).

The suggested ordering of these systems is `Acceleration -> Capping -> Movement -> Damping`.

## Collisions

[`CollisionSystem`](src/main/java/dev/kkorolyov/pancake/core/system/CollisionSystem.java) simulates elastic collisions between [`Intersected`](src/main/java/dev/kkorolyov/pancake/core/component/event/Intersected.java) [`Collidable`](src/main/java/dev/kkorolyov/pancake/core/component/tag/Collidable.java) entities calculated from their [`Velocity`](src/main/java/dev/kkorolyov/pancake/core/component/movement/Velocity.java) (and optionally, [`Force`](src/main/java/dev/kkorolyov/pancake/core/component/movement/Force.java) and [`Mass`](src/main/java/dev/kkorolyov/pancake/core/component/movement/Mass.java)).

[`CorrectionSystem`](src/main/java/dev/kkorolyov/pancake/core/system/CorrectionSystem.java) repositions [`Intersected`](src/main/java/dev/kkorolyov/pancake/core/component/event/Intersected.java) [`Correctable`](src/main/java/dev/kkorolyov/pancake/core/component/tag/Correctable.java) entities just enough to remove the intersection between them.

[`IntersectionSystem`](src/main/java/dev/kkorolyov/pancake/core/system/IntersectionSystem.java) detects entity intersections given their [`Position`](src/main/java/dev/kkorolyov/pancake/core/component/Position.java) and [`Bounds`](src/main/java/dev/kkorolyov/pancake/core/component/Bounds.java).
Applies an [`Intersected`](src/main/java/dev/kkorolyov/pancake/core/component/event/Intersected.java) event component to each intersecting entity.

[`PhysicsCleanupSystem`](src/main/java/dev/kkorolyov/pancake/core/system/cleanup/PhysicsCleanupSystem.java) removes [`Intersected`](src/main/java/dev/kkorolyov/pancake/core/component/event/Intersected.java) event components from entities.

The suggested ordering of these systems is `Intersection -> Correction -> Collision -> PhysicsCleanup`.

## Misc

[`ActionSystem`](src/main/java/dev/kkorolyov/pancake/core/system/ActionSystem.java) dequeues and applies actions from an entity's [`ActionQueue`](src/main/java/dev/kkorolyov/pancake/core/component/ActionQueue.java).

[`ChainSystem`](src/main/java/dev/kkorolyov/pancake/core/system/ChainSystem.java) repositions an entity's [`Position`](src/main/java/dev/kkorolyov/pancake/core/component/Position.java) in accordance to its [`Chain`](src/main/java/dev/kkorolyov/pancake/core/component/Chain.java) to another entity.

[`SpawnSystem`](src/main/java/dev/kkorolyov/pancake/core/system/SpawnSystem.java) creates new entities originating at a given entity's [`Position`](src/main/java/dev/kkorolyov/pancake/core/component/Position.java) and according to its [`Spawner`](src/main/java/dev/kkorolyov/pancake/core/component/Spawner.java).

---

# See also

- How to
	- [Use with Gradle](howto-gradle.md)
