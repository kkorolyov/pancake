package dev.kkorolyov.pancake.core.editor.test.e2e

import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.core.component.Bounds
import dev.kkorolyov.pancake.core.component.Damping
import dev.kkorolyov.pancake.core.component.Force
import dev.kkorolyov.pancake.core.component.Mass
import dev.kkorolyov.pancake.core.component.Path
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.core.component.Velocity
import dev.kkorolyov.pancake.core.component.limit.VelocityLimit
import dev.kkorolyov.pancake.core.system.AccelerationSystem
import dev.kkorolyov.pancake.core.system.ActionSystem
import dev.kkorolyov.pancake.core.system.CollisionSystem
import dev.kkorolyov.pancake.core.system.DampingSystem
import dev.kkorolyov.pancake.core.system.GoSystem
import dev.kkorolyov.pancake.core.system.IntersectionSystem
import dev.kkorolyov.pancake.core.system.LimitSystem
import dev.kkorolyov.pancake.core.system.MovementSystem
import dev.kkorolyov.pancake.core.system.PathSystem
import dev.kkorolyov.pancake.core.system.cleanup.PhysicsCleanupSystem
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
				CollisionSystem(),
				DampingSystem(),
				IntersectionSystem(),
				MovementSystem(),
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
					Transform().apply {
						parent = Transform().apply { translation.set(randVector()) }
						translation.set(randVector())
					},
					Damping(Vector3.of(Random.nextDouble(1.0), Random.nextDouble(1.0), Random.nextDouble(1.0))),
					Force().apply {
						value.set(randVector())
						offset.set(randVector())
					},
					Mass(Random.nextDouble()),
					Velocity().apply {
						linear.set(randVector())
						angular.set(randVector())
					},
					VelocityLimit(Random.nextDouble(100.0), Random.nextDouble(100.0)),
					Path(Random.nextDouble(10.0), Random.nextDouble(10.0), Path.SnapStrategy.values()[Random.nextInt(Path.SnapStrategy.values().size)])
				)
			}
		}
	})
}
