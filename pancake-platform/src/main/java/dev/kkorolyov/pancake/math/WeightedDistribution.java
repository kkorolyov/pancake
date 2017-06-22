package dev.kkorolyov.pancake.math;

import java.util.NavigableMap;
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
	 */
	public void add(int weight, T value) {
		distribution.put(total, value);
		total += weight;
	}

	/** @return random value from this distribution */
	public T get() {
		return distribution.floorEntry(rand.nextInt(total))
											 .getValue();
	}
}
