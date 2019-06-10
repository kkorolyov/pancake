package dev.kkorolyov.killstreek.item

import dev.kkorolyov.killstreek.media.Sprite
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.simplestructs.WeightedDistribution

/** An effect which does nothing, used for padding the pool of random effects */
val NOOP_EFFECT: Action = Action {}

private var idCounter = 0

/**
 * Applies effects to entities.
 * @constructor constructs a new item with a unique ID
 */
abstract class Item(
		/** item name */
		val name: String,
		/** item visual */
		val sprite: Sprite
) : Action {
	/** unique item ID */
	val id = idCounter++
	private val effects: WeightedDistribution<Action> = WeightedDistribution()

	/** maximum number of items of this type that may exist in a single stack */
	abstract val maxStackSize: Int

	/**
	 * Applies a randomly-selected effect from this item's effect pool to [entity].
	 */
	override fun apply(entity: Entity) {
		effects.get().apply(entity)
	}

	/**
	 * Adds [effect] with selection [weight] to this item and returns this item.
	 */
	fun addEffect(effect: Action, weight: Int): Item {
		effects.add(effect, weight)
		return this
	}
}
