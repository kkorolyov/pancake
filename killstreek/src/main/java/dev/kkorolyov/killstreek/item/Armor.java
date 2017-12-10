package dev.kkorolyov.killstreek.item;

import dev.kkorolyov.killstreek.media.Sprite;
import dev.kkorolyov.pancake.platform.math.BoundedValue;

/**
 * A defensive equippable.
 */
public class Armor extends Equippable {
	private final Type type;

	/**
	 * Constructs a new armor piece.
	 * @param name armor name
	 * @param sprite armor visual
	 * @param durability armor durability
	 * @param value defense value
	 * @param type armor type
	 */
	public Armor(String name, Sprite sprite, BoundedValue<Integer> durability, int value, Type type) {
		super(name, sprite, durability, value);

		this.type = type;
	}

	/** @return armor type */
	public Type getType() {
		return type;
	}

	/**
	 * All armor types.
	 */
	public enum Type {
		HEAD,
		TORSO,
		ARM_L,
		ARM_R,
		LEG_L,
		LEG_R
	}
}
