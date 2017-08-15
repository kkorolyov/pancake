package dev.kkorolyov.killstreek.item;

import dev.kkorolyov.killstreek.utility.BoundedValue;
import dev.kkorolyov.pancake.component.Sprite;

/**
 * An offensive equippable.
 */
public class Weapon extends Equippable {
	private final float rate;
	private final int requiredSlots;

	/**
	 * Constructs a new weapon.
	 * @param name weapon name
	 * @param sprite weapon visual
	 * @param durability weapon durability
	 * @param value damage value
	 * @param rate weapon fire rate in s
	 * @param requiredSlots number of weapon slots required to equip this weapon
	 */
	public Weapon(String name, Sprite sprite, BoundedValue<Integer> durability, int value, int rate, int requiredSlots) {
		super(name, sprite, durability, value);

		this.rate = rate;
		this.requiredSlots = requiredSlots;
	}

	/** @return fire rate in s */
	public float getRate() {
		return rate;
	}

	/** @return number of weapon slots required to equip this weapon */
	public int getRequiredSlots() {
		return requiredSlots;
	}
}
