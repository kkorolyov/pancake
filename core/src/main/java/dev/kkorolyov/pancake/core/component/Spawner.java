package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.flub.data.WeightedDistribution;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Provides clones of entity templates between 2 radii around some position.
 * @deprecated application-specific, should be removed from core library
 */
@Deprecated
public final class Spawner implements Component {
	private static final double PI = Math.PI;
	private static final Random rand = new Random();

	private double minRadius;
	private double radiusDifference;

	private double interval;
	private double sinceLast;

	private WeightedDistribution<Supplier<Iterable<Component>>> templates;

	/**
	 * Constructs a new spawner.
	 * @param minRadius radius past which clones are spawned
	 * @param maxRadius radius within which clones are spawned
	 * @param interval minimum seconds between spawns when active; negative implies inactive with {@code abs(interval)} interval
	 * @param templates generators of randomly-selected templates to clone and spawn; each template should contain a {@link Position} where non-zero position components denote axes to randomize
	 */
	public Spawner(double minRadius, double maxRadius, double interval, WeightedDistribution<Supplier<Iterable<Component>>> templates) {
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
	public Iterable<Component> spawn(Vector3 origin, double dt) {
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
	public Iterable<Component> spawn(Vector3 origin) {
		Iterable<Component> clone = templates.get().get();

		for (Component component : clone) {
			if (component instanceof Position) {
				Vector3 position = ((Position) component).getValue();
				randomPosition(position);
				position.add(origin);

				break;
			}
		}
		return clone;
	}
	private void randomPosition(Vector3 position) {
		double radius = minRadius + (radiusDifference * rand.nextDouble());
		double theta = 2 * PI * rand.nextDouble();
		double u = (rand.nextBoolean() ? 1 : -1) * rand.nextDouble();
		double v = radius * Math.sqrt(1 - u * u);

		double x = v * Math.cos(theta);
		double y = v * Math.sin(theta);
		double z = radius * u;

		if (position.getX() != 0) position.setX(x);
		if (position.getY() != 0) position.setY(y);
		if (position.getZ() != 0) position.setZ(z);
	}

	/**
	 * Toggles active status.
	 */
	public void toggle() {
		setActive(!isActive());
	}

	/** @return {@code true} if this spawner provides clones at a regular interval */
	public boolean isActive() {
		return interval > 0;
	}
	/** @param active {@code true} enables spawns at a regular interval */
	public void setActive(boolean active) {
		if (isActive() != active) interval *= -1;
	}

	/**
	 * @param minRadius radius past which clones are spawned
	 * @param maxRadius radius within which clones are spawned
	 */
	public void setRadius(double minRadius, double maxRadius) {
		this.minRadius = Math.max(0, minRadius);
		radiusDifference = Math.max(this.minRadius, maxRadius) - this.minRadius;
	}

	/** @return minimum seconds between spawns when active; negative implies inactive with {@code abs(interval)} interval */
	public double getInterval() {
		return interval;
	}
	/** @param interval new minimum seconds between spawns when active; negative implies inactive with {@code abs(interval)} interval */
	public void setInterval(double interval) {
		this.interval = Math.abs(interval);
		sinceLast = 0;
	}

	/** @return distribution of cloned templates */
	public WeightedDistribution<Supplier<Iterable<Component>>> getTemplates() {
		return templates;
	}
	/** @param templates generators of randomly-selected templates to clone and spawn; each template should contain a {@link Position} where non-zero position components denote axes to randomize */
	public void setTemplates(WeightedDistribution<Supplier<Iterable<Component>>> templates) {
		this.templates = templates;
	}
}
