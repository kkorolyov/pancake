package dev.kkorolyov.pancake.platform.utility;

import dev.kkorolyov.pancake.platform.Config;

/**
 * Samples time between invocations of a sampling method.
 */
public final class Sampler {
	private final AveragedValue tick;

	private long last;

	/**
	 * Constructs a new Sampler with sample count from the platform config's {@code samples} property.
	 * @see Config#get()
	 */
	public Sampler() {
		this(Math.max(1, Integer.parseInt(Config.get().getProperty("samples"))));
	}
	/**
	 * Constructs a new Sampler for {@code count} samples.
	 */
	public Sampler(int count) {
		tick = new AveragedValue(count);
	}

	/**
	 * Samples time elapsed since the last invocation of {@link #reset()}.
	 * Also resets this sampler's counter.
	 */
	public void sample() {
		tick.add(System.nanoTime() - last);
		reset();
	}

	/**
	 * Resets this sampler's counter.
	 */
	public void reset() {
		last = System.nanoTime();
	}

	/**
	 * Returns the duration in ns between sampling averaged over all samples.
	 */
	public long getValue() {
		return tick.get();
	}
	/**
	 * Returns the maximum number of samples taken for an average value.
	 */
	public int getCount() {
		return tick.size();
	}
}
