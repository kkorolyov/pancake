package dev.kkorolyov.killstreek.item;

import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.math.WeightedDistribution;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Applies effects to entities.
 */
public class Item {
	private final String name;
	private final Sprite sprite;
	private final WeightedDistribution<Consumer<Entity>> effects = new WeightedDistribution<>();
	private int uses;

	/**
	 * Constructs a new item with {@code 1} use.
	 * @see #Item(String, Sprite, int)
	 */
	public Item(String name, Sprite sprite) {
		this(name, sprite, 1);
	}
	/**
	 * Constructs a new item.
	 * @param name item name
	 * @param sprite item visual
	 * @param uses initial number of item uses
	 */
	public Item(String name, Sprite sprite, int uses) {
		this.name = name;
		this.sprite = sprite;
		setUses(uses);
	}

	/**
	 * If this item has remaining uses, applies a random effect from this item to an entity and decrements remaining uses by {@code 1}.
	 * @param entity entity receiving effect
	 * @return {@code true} if this item had at least 1 use and affected {@code entity}
	 * @throws NoSuchElementException if this item has remaining uses, but no effects
	 */
	public boolean apply(Entity entity) {
		if (isEmpty()) return false;

		effects.get().accept(entity);
		uses--;
		return true;
	}

	/**
	 * Adds an effect with weight 1 to this item.
	 * @param effect effect on entity
	 * @return {@code this}
	 */
	public Item addEffect(Consumer<Entity> effect) {
		return addEffect(effect, 1);
	}
	/**
	 * Adds an effect to this item.
	 * @param effect effect on entity
	 * @param weight effect frequency in relation to all other effects of this item
	 * @return {@code this}
	 */
	public Item addEffect(Consumer<Entity> effect, int weight) {
		effects.add(weight, effect);
		return this;
	}

	/** @return {@code true} if this item has no more uses remaining */
	public boolean isEmpty() {
		return getUses() <= 0;
	}

	/** @return item name */
	public String getName() {
		return name;
	}

	/** @return item sprite */
	public Sprite getSprite() {
		return sprite;
	}

	/** @return number of remaining uses */
	public int getUses() {
		return uses;
	}
	/**
	 * @param uses new number of remaining uses
	 * @return {@code this}
	 */
	public Item setUses(int uses) {
		this.uses = uses;
		return this;
	}
}
