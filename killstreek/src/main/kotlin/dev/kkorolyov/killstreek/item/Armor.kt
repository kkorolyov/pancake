package dev.kkorolyov.killstreek.item

import dev.kkorolyov.killstreek.media.Sprite
import dev.kkorolyov.pancake.platform.math.BoundedValue

/**
 * A defensive equippable.
 * @constructor constructs a new armor piece with a piece [type]
 */
class Armor(
	name: String,
	sprite: Sprite,
	durability: BoundedValue<Int>,
	value: Int,
	/** armor type */
	val type: Type
) : Equippable(name, sprite, durability, value) {
	/**
	 * A type of armor piece.
	 */
	enum class Type {
		HEAD,
		TORSO,
		ARM_L,
		ARM_R,
		LEG_L,
		LEG_R
	}
}
