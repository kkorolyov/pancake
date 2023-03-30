package dev.kkorolyov.pancake.editor.core.test.e2e

import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.core.component.Position
import dev.kkorolyov.pancake.core.component.movement.Force
import dev.kkorolyov.pancake.core.component.movement.Velocity
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.test.SpecUtilities.randVector
import kotlin.random.Random

private val actions = listOf(
	Action { it[Force::class.java].value.add(randVector()) },
	Action { it[Velocity::class.java].value.set(randVector()) },
	Action { it[Position::class.java].value.set(randVector()) }
)

/**
 * Enqueues a randomized set of actions to entities.
 */
class ActionEnqueuerSystem : GameSystem(ActionQueue::class.java) {
	override fun update(entity: Entity, dt: Long) {
		(1..Random.nextInt(11)).map {
			entity[ActionQueue::class.java].add(actions.random())
		}
	}
}
