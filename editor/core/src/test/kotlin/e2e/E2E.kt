package e2e

import dev.kkorolyov.pancake.core.system.AccelerationSystem
import dev.kkorolyov.pancake.core.system.ActionSystem
import dev.kkorolyov.pancake.core.system.CappingSystem
import dev.kkorolyov.pancake.core.system.ChainSystem
import dev.kkorolyov.pancake.core.system.CollisionSystem
import dev.kkorolyov.pancake.core.system.DampingSystem
import dev.kkorolyov.pancake.core.system.IntersectionSystem
import dev.kkorolyov.pancake.core.system.MovementSystem
import dev.kkorolyov.pancake.core.system.SpawnSystem
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.Pipeline

fun main() {
	start(GameEngine().apply {
		setPipelines(
			Pipeline(
				AccelerationSystem(),
				ActionSystem(),
				CappingSystem(),
				ChainSystem(),
				CollisionSystem(),
				DampingSystem(),
				IntersectionSystem(),
				MovementSystem(),
				SpawnSystem()
			),
			Pipeline(
				drawStart(),
				editor(this),
				drawEnd()
			)
		)
	})
}
