package dev.kkorolyov.pancake.platform.math;

import java.util.Arrays;

/**
 * A value represented as the average of its previous several samples.
 */
public class AveragedValue {
	private long[] samples;
	private long value;
	private int counter;

	/**
	 * Constructs an averaged value with all 0 samples.
	 * @param samples number of individual samples
	 */
	public AveragedValue(int samples) {
		this.samples = new long[samples];
	}

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
	public long getValue() {
		return value;
	}

	/** @return maximum number of samples */
	public int getSamples() {
		return samples.length;
	}
	/**
	 * @param samples maximum number of samples
	 * @return {@code this}
	 */
	public AveragedValue setSamples(int samples) {
		this.samples = Arrays.copyOf(this.samples, samples);
		return this;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
