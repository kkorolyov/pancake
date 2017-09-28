package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.math.WeightedDistribution;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Provides clones of entity templates between 2 radii around some position.
 */
public class Spawner implements Component {
	private static final float PI = (float) Math.PI;
	private static final Random rand = new Random();

	private float minRadius;
	private float radiusDifference;

	private float interval;
	private float sinceLast;

	private WeightedDistribution<Supplier<Iterable<Component>>> templates;

	/**
	 * Constructs a new spawner.
	 * @param minRadius radius past which clones are spawned
	 * @param maxRadius radius within which clones are spawned
	 * @param interval minimum seconds between spawns when active; negative implies inactive with {@code abs(interval)} interval
	 * @param templates generators of randomly-selected templates to clone and spawn; each template should contain a {@link Transform} where non-zero position components denote axes to randomize
	 */
	public Spawner(float minRadius, float maxRadius, float interval, WeightedDistribution<Supplier<Iterable<Component>>> templates) {
		setRadius(minRadius, maxRadius);
		setInterval(interval);
		setTemplates(templates);
	}

	/**
	 * Creates, positions, and returns a template clone.
	 * @param origin point around which to randomly position clone
	 * @param dt seconds since last invocation of this method
	 * @return random template clone randomly positioned around {@code origin}, or {@code null} if inactive or the last invocation of this method happened within the spawn interval
	 */
	public Iterable<Component> spawn(Vector origin, float dt) {
		if (!isActive()) return null;

		sinceLast += dt;
		if (sinceLast < interval) return null;

		sinceLast = 0;
		return spawn(origin);
	}
	/**
	 * Creates, positions, and returns a template clone.
	 * @param origin point around which to randomly position clone
	 * @return random template clone randomly positioned around {@code origin}
	 */
	public Iterable<Component> spawn(Vector origin) {
		Iterable<Component> clone = templates.get().get();

		for (Component component : clone) {
			if (component instanceof Transform) {
				Vector position = ((Transform) component).getPosition();
				randomPosition(position);
				position.add(origin);

				break;
			}
		}
		return clone;
	}
	private void randomPosition(Vector position) {
		float radius = minRadius + (radiusDifference * rand.nextFloat());
		float theta = 2 * PI * rand.nextFloat();
		float phi = 2 * PI * rand.nextFloat();

		float x = (float) (radius * Math.cos(theta));
		float y = (float) (radius * Math.sin(theta));
		float z = (float) (radius * Math.sin(phi));

		if (position.getX() != 0) position.setX(x);
		if (position.getY() != 0) position.setY(y);
		if (position.getZ() != 0) position.setZ(z);
	}

	/** @return {@code true} if this spawner provides clones at a regular interval */
	public boolean isActive() {
		return interval > 0;
	}
	/** @param active	{@code true} enables spawns at a regular interval */
	public void setActive(boolean active) {
		if (isActive() != active) interval *= -1;
	}

	/**
	 * @param minRadius radius past which clones are spawned
	 * @param maxRadius radius within which clones are spawned
	 */
	public void setRadius(float minRadius, float maxRadius) {
		this.minRadius = Math.max(0, minRadius);
		radiusDifference = Math.max(this.minRadius, maxRadius) - this.minRadius;
	}

	/** @return minimum seconds between spawns when active; negative implies inactive with {@code abs(interval)} interval */
	public float getInterval() {
		return interval;
	}
	/** @param interval new minimum seconds between spawns when active; negative implies inactive with {@code abs(interval)} interval */
	public void setInterval(float interval) {
		this.interval = Math.abs(interval);
		sinceLast = 0;
	}

	/** @return distribution of cloned templates */
	public WeightedDistribution<Supplier<Iterable<Component>>> getTemplates() {
		return templates;
	}
	/** @param templates generators of randomly-selected templates to clone and spawn; each template should contain a {@link Transform} where non-zero position components denote axes to randomize */
	public void setTemplates(WeightedDistribution<Supplier<Iterable<Component>>> templates) {
		this.templates = templates;
	}
}
