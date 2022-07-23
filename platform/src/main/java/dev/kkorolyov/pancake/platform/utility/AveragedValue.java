package dev.kkorolyov.pancake.platform.utility;

import java.util.Arrays;

/**
 * A value represented as the average of its previous several samples.
 */
public final class AveragedValue {
	private final long[] samples;
	private double value;
	private int counter;

	/**
	 * Constructs an averaged value with all 0 samples.
	 * @param count maximum number of individual samples; must be {@code > 0}
	 */
	public AveragedValue(int count) {
		if (count <= 0) {
			throw new IllegalArgumentException("samples must be > 0");
		}
		samples = new long[count];
	}

	/**
	 * Adds a sample to this value.
	 * @param sample sample to add
	 */
	public void add(long sample) {
		value -= ((double) samples[counter]) / samples.length;
		value += ((double) (samples[counter] = sample)) / samples.length;

		counter = (counter < samples.length - 1) ? counter + 1 : 0;
	}

	/** @return average value of current samples */
	public long get() {
		return (long) value;
	}

	/** @return maximum number of samples */
	public int size() {
		return samples.length;
	}

	@Override
	public String toString() {
		return "AveragedValue{" +
				"samples=" + Arrays.toString(samples) +
				", value=" + value +
				", counter=" + counter +
				'}';
	}
}
