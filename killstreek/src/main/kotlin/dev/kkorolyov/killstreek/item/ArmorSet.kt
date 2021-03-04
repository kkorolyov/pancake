package dev.kkorolyov.killstreek.item

import dev.kkorolyov.killstreek.item.Armor.Type
import java.util.EnumMap

/**
 * A collection of armor pieces equipped to slots.
 * @constructor constructs a new armor set with initial [armor] pieces
 */
class ArmorSet(armor: Iterable<Armor>) {
	private val _armor: MutableMap<Type, Armor> = EnumMap(Type::class.java)

	/** Current armor pieces */
	val armor: Collection<Armor> get() = _armor.values

	/** total defense value of all unbroken armor */
	val defense: Int = this._armor.values
		.filter { !it.isBroken }
		.sumBy { it.value }

	init {
		armor.forEach { equip(it) }
	}

	/** see [equip] */
	operator fun plusAssign(armor: Armor) {
		equip(armor)
	}

	/** see [unequip] */
	operator fun minusAssign(type: Type) {
		unequip(type)
	}

	/**
	 * Equips [armor] and returns previously equipped piece of the same type.
	 */
	fun equip(armor: Armor): Armor? = _armor.put(armor.type, armor)

	/**
	 * Removes and returns current equipped armor of [type].
	 */
	fun unequip(type: Type): Armor? = _armor.remove(type)
}
