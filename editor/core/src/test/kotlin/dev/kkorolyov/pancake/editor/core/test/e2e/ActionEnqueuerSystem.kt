package dev.kkorolyov.pancake.editor.core.test.e2e

import dev.kkorolyov.pancake.core.action.ForceAction
import dev.kkorolyov.pancake.core.action.PositionAction
import dev.kkorolyov.pancake.core.action.VelocityAction
import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.test.SpecUtilities.randVector
import kotlin.random.Random

private fun randAction() = listOf(
	{ ForceAction(randVector()) },
	{ PositionAction(randVector()) },
	{ VelocityAction(randVector()) }
).random()()

/**
 * Enqueues a randomized set of actions to entities.
 */
class ActionEnqueuerSystem : GameSystem(ActionQueue::class.java) {
	override fun update(entity: Entity, dt: Long) {
		entity[ActionQueue::class.java].enqueue(
			(1..Random.nextInt(11)).map {
				randAction()
			}
		)
	}
}
