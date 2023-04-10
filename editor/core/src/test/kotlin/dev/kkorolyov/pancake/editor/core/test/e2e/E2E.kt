package dev.kkorolyov.pancake.editor.core.test.e2e

import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.core.component.Bounds
import dev.kkorolyov.pancake.core.component.Chain
import dev.kkorolyov.pancake.core.component.Path
import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.core.component.limit.VelocityLimit
import dev.kkorolyov.pancake.core.component.movement.Damping
import dev.kkorolyov.pancake.core.component.movement.Force
import dev.kkorolyov.pancake.core.component.movement.Mass
import dev.kkorolyov.pancake.core.component.movement.Velocity
import dev.kkorolyov.pancake.core.system.AccelerationSystem
import dev.kkorolyov.pancake.core.system.ActionSystem
import dev.kkorolyov.pancake.core.system.ChainSystem
import dev.kkorolyov.pancake.core.system.CollisionSystem
import dev.kkorolyov.pancake.core.system.DampingSystem
import dev.kkorolyov.pancake.core.system.GoSystem
import dev.kkorolyov.pancake.core.system.IntersectionSystem
import dev.kkorolyov.pancake.core.system.MovementSystem
import dev.kkorolyov.pancake.core.system.PathSystem
import dev.kkorolyov.pancake.core.system.SpawnSystem
import dev.kkorolyov.pancake.core.system.cleanup.PhysicsCleanupSystem
import dev.kkorolyov.pancake.core.system.limit.LimitSystem
import dev.kkorolyov.pancake.editor.test.drawEnd
import dev.kkorolyov.pancake.editor.test.drawStart
import dev.kkorolyov.pancake.editor.test.editor
import dev.kkorolyov.pancake.editor.test.start
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.Pipeline
import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.test.SpecUtilities.randVector
import kotlin.random.Random

fun main() {
	start(GameEngine().apply {
		setPipelines(
			Pipeline.of(
				PathSystem(),
				GoSystem(),
				AccelerationSystem(),
				ActionSystem(),
				LimitSystem(Velocity::class.java, VelocityLimit::class.java),
				ChainSystem(),
				CollisionSystem(),
				DampingSystem(),
				IntersectionSystem(),
				MovementSystem(),
				SpawnSystem()
			),
			Pipeline.of(
				PhysicsCleanupSystem()
			),
			Pipeline.of(
				ActionEnqueuerSystem()
			),
			Pipeline.of(
				drawStart(),
				editor(this),
				drawEnd()
			)
		)

		entities.apply {
			(1..10).map {
				create().put(
					ActionQueue(),
					if (Random.nextBoolean()) Bounds.box(randVector()) else Bounds.round(Random.nextDouble()),
					Chain(randVector(), Random.nextDouble(), if (Random.nextBoolean()) listOf(randVector()) else listOf()),
					Position(randVector()).apply { parent = Position(randVector()) },
					Damping(Vector3.of(Random.nextDouble(1.0), Random.nextDouble(1.0), Random.nextDouble(1.0))),
					Force(randVector()),
					Mass(Random.nextDouble()),
					Velocity(randVector()),
					VelocityLimit(Random.nextDouble(100.0)),
					Path(Random.nextDouble(10.0), Random.nextDouble(10.0), Path.SnapStrategy.values()[Random.nextInt(Path.SnapStrategy.values().size)])
				)
			}
		}
	})
}
