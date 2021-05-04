package dev.kkorolyov.pancake.platform.math;

import java.util.Arrays;

/**
 * A value represented as the average of its previous several samples.
 */
public final class AveragedValue {
	private final long[] samples;
	private long value;
	private int counter;

	/**
	 * Constructs an averaged value with all 0 samples.
	 * @param samples number of individual samples; must be {@code > 0}
	 */
	public AveragedValue(int samples) {
		if (samples <= 0) {
			throw new IllegalArgumentException("samples must be > 0");
		}
		this.samples = new long[samples];
	}

	/**
	 * Adds a sample to this value.
	 * @param sample sample to add
	 */
	public void add(long sample) {
		samples[counter++] = sample;

		if (counter >= samples.length) {
			value = 0;
			for (long s : samples) value += s;
			value /= samples.length;

			counter = 0;
		}
	}

	/** @return average value of current samples */
	public long get() {
		return value;
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
