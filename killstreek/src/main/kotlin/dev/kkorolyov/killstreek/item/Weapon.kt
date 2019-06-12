package dev.kkorolyov.killstreek.item

import dev.kkorolyov.killstreek.media.Sprite
import dev.kkorolyov.pancake.platform.math.BoundedValue

/**
 * An offensive [Equippable],
 * @constructor constructs a new weapon with attack [rate] and [requiredSlots] to equip
 */
class Weapon(
		name: String,
		sprite: Sprite,
		durability: BoundedValue<Int>,
		value: Int,
		/** attack rate in `ns` */
		val rate: Int,
		/** number of weapon slots required to equip this weapon */
		val requiredSlots: Int
) : Equippable(name, sprite, durability, value)
