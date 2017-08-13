package dev.kkorolyov.killstreek.item;

import dev.kkorolyov.killstreek.utility.BoundedValue;
import dev.kkorolyov.pancake.component.Sprite;

/**
 * An offensive equippable.
 */
public class Weapon extends Equippable {
	private final float rate;
	private boolean twoHanded;

	/**
	 * Constructs a new weapon.
	 * @param name weapon name
	 * @param sprite weapon visual
	 * @param durability weapon durability
	 * @param value damage value
	 * @param rate weapon fire rate in s
	 * @param twoHanded whether this weapon requires both weapon slots to equip
	 */
	public Weapon(String name, Sprite sprite, BoundedValue<Integer> durability, int value, int rate, boolean twoHanded) {
		super(name, sprite, durability, value);

		this.rate = rate;
		this.twoHanded = twoHanded;
	}

	/** @return fire rate in s */
	public float getRate() {
		return rate;
	}

	/** @return {@code true} if this weapon requires both weapon slots to equip */
	public boolean isTwoHanded() {
		return twoHanded;
	}
}
