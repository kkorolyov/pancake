package dev.kkorolyov.killstreek.item

import dev.kkorolyov.killstreek.media.Sprite
import dev.kkorolyov.pancake.platform.math.BoundedValue

/**
 * A finite-durability [Item] attachable to an equipment slot.
 * @constructor constructs a new equippable with [name], [sprite] visual, [durability], and [value]
 */
abstract class Equippable(
		name: String,
		sprite: Sprite,
		/** level of breakage */
		val durability: BoundedValue<Int>,
		/** action value */
		val value: Int
) : Item(name, sprite) {
	/** whether current durability is equal to its minimum value */
	val isBroken: Boolean get() = durability.get() == durability.minimum
	override val maxStackSize: Int = 1

	/**
	 * Translates current durability by up to [amount] and returns the updated durability.
	 */
	fun changeDurability(amount: Int): Int {
		durability.set(durability.get() + amount)
		return durability.get()
	}
}
