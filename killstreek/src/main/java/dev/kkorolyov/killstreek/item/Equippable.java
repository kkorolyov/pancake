package dev.kkorolyov.killstreek.item;

import dev.kkorolyov.killstreek.utility.BoundedValue;
import dev.kkorolyov.pancake.component.Sprite;

/**
 * An item attachable to an equipment slot.
 * Has durability and breaks after enough uses.
 */
public abstract class Equippable extends Item {
	private final BoundedValue<Integer> durability;
	private final int value;

	/**
	 * Constructs a new equippable.
	 * @param name equippable name
	 * @param sprite equippable visual
	 * @param durability equippable durability
	 * @param value damage/defense value
	 */
	public Equippable(String name, Sprite sprite, BoundedValue<Integer> durability, int value) {
		super(name, sprite);

		this.durability = durability;
		this.value = value;
	}

	/**
	 * Applies a change to current durability.
	 * @param amount change in current durability
	 */
	public void changeDurability(int amount) {
		durability.set(durability.get() + amount);
	}

	/** @return {@code true} if current durability value is equal to the minimum value */
	public boolean isBroken() {
		return durability.get().equals(durability.getMinimum());
	}

	/** @return durability value */
	public BoundedValue<Integer> getDurability() {
		return durability;
	}

	/** @return damage/defense value */
	public int getValue() {
		return value;
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}
}
