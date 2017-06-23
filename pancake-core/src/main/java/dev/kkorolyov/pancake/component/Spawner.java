package dev.kkorolyov.pancake.component;

import java.util.function.Supplier;

import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.math.Vector;
import dev.kkorolyov.pancake.math.WeightedDistribution;

/**
 * Provides clones of entity templates between 2 radii around some position.
 */
public class Spawner implements Component {
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
	 * Returns a template clone, or {@code null} if inactive or the spawn interval has not yet passed since the last spawn.
	 * @param origin point around which to position clone
	 * @param dt seconds since last invocation of this method
	 * @return random template clone positioned around {@code origin}, or {@code null} if not allowed
	 */
	public Iterable<Component> spawn(Vector origin, float dt) {
		if (!isActive()) return null;

		sinceLast += dt;
		if (sinceLast < interval) return null;

		sinceLast = 0;
		return spawn(origin);
	}
	/**
	 * Returns a template clone.
	 * @param origin point around which to position clone
	 * @return random template clone positioned around {@code origin}
	 */
	public Iterable<Component> spawn(Vector origin) {
		Iterable<Component> clone = templates.get().get();

		for (Component component : clone) {
			if (component instanceof Transform) {
				Vector position = ((Transform) component).getPosition();
				position(position);
				position.add(origin);

				break;
			}
		}
		return clone;
	}
	private void position(Vector position) {
		float radius = (float) (minRadius + (radiusDifference * Math.random()));
		float theta = (float) (2 * Math.PI * Math.random());
		float phi = (float) (2 * Math.PI * Math.random());

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
	/** @param interval minimum seconds between spawns when active; negative implies inactive with {@code abs(interval)} interval */
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
