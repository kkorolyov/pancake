package dev.kkorolyov.killstreek.item;

import dev.kkorolyov.killstreek.item.Armor.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A collection of armor pieces equipped to slots.
 */
public class ArmorSet {
	private final Map<Type, Armor> armor = new HashMap<>();

	/** @return total defense value of all unbroken armor */
	public int getDefense() {
		return armor.values().stream()
				.filter(e -> !e.isBroken())
				.mapToInt(Equippable::getValue)
				.sum();
	}

	/** @return equipped armor pieces */
	public Iterable<Armor> getArmor() {
		return armor.values();
	}
	/**
	 * @param broken {@code true} filters to broken armor, {@code false} filters to unbroken armor
	 * @return filtered equipped armor pieces
	 */
	public Iterable<Armor> getArmor(boolean broken) {
		return armor.values().stream()
				.filter(e -> e.isBroken() == broken)
				.collect(Collectors.toList());
	}

	/**
	 * @param armor new equipped armor piece
	 * @return previous equipped armor of the same type as {@code armor}, or {@code null} if no such type was equipped
	 */
	public Armor equip(Armor armor) {
		return this.armor.put(armor.getType(), armor);
	}
	/**
	 * @param type type of armor to unequip
	 * @return unequipped armor, or {@code null} if no such type was equipped
	 */
	public Armor unequip(Armor.Type type) {
		return armor.remove(type);
	}
}
