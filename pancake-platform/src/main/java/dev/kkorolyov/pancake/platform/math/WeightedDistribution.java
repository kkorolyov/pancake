package dev.kkorolyov.pancake.platform.math;

import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.TreeMap;

/**
 * A distribution of randomly-selectable weighted values.
 */
public class WeightedDistribution<T> {
	private final NavigableMap<Integer, T> distribution = new TreeMap<>();
	private int total;
	private final Random rand = new Random();

	/**
	 * Adds a weighted value to this distribution.
	 * @param weight value weight relative to this retriever's total weight
	 * @param value added value
	 * @return {@code this}
	 */
	public WeightedDistribution<T> add(int weight, T value) {
		distribution.put(total, value);
		total += weight;

		return this;
	}

	/**
	 * @return random value from this distribution
	 * @throws NoSuchElementException if this distribution is empty
	 */
	public T get() {
		if (total == 0) throw new NoSuchElementException("Distribution is empty");

		return distribution.floorEntry(rand.nextInt(total)).getValue();
	}
}
