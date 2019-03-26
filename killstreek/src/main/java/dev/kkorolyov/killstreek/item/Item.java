package dev.kkorolyov.killstreek.item;

import dev.kkorolyov.killstreek.media.Sprite;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.simplestructs.WeightedDistribution;

import java.util.NoSuchElementException;

/**
 * Applies effects to entities.
 */
public abstract class Item {
	/** An effect which does nothing, used for padding the pool of random effects */
	public static final Action NOOP_EFFECT = entity -> {};
	private static int idCounter;

	private static int generateId() {
		return idCounter++;
	}

	private final int id;
	private final String name;
	private final Sprite sprite;
	private final WeightedDistribution<Action> effects = new WeightedDistribution<>();

	/**
	 * Constructs a new item with a unique ID.
	 * @param name item name
	 * @param sprite item visual
	 */
	public Item(String name, Sprite sprite) {
		this.id = generateId();
		this.name = name;
		this.sprite = sprite;
	}

	/**
	 * Applies a randomly-select effect from this item's effect pool to an entity.
	 * @param entity entity receiving effect
	 * @throws NoSuchElementException if this item has no effects
	 */
	public void apply(Entity entity) {
		effects.get().apply(entity);
	}

	/**
	 * Adds an effect with weight 1 to this item.
	 * @param effect effect on entity
	 * @return {@code this}
	 */
	public Item addEffect(Action effect) {
		return addEffect(effect, 1);
	}
	/**
	 * Adds an effect to this item.
	 * @param effect effect on entity
	 * @param weight effect frequency in relation to all other effects of this item
	 * @return {@code this}
	 */
	public Item addEffect(Action effect, int weight) {
		effects.add(effect, weight);
		return this;
	}

	/** @return unique item ID */
	public int getId() {
		return id;
	}

	/** @return item name */
	public String getName() {
		return name;
	}

	/** @return item sprite */
	public Sprite getSprite() {
		return sprite;
	}

	/** @return maximum number of items of this type in a single stack */
	public abstract int getMaxStackSize();
}
