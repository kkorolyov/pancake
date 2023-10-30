package dev.kkorolyov.pancake.platform.utility;

/**
 * Samples time elapsed between invocations of a sampling method.
 */
public final class Sampler {
	private final AveragedValue tick;

	private long start;

	/**
	 * Constructs a new Sampler with a sample count of {@code 30}.
	 */
	public Sampler() {
		this(30);
	}
	/**
	 * Constructs a new Sampler for {@code count} samples.
	 */
	public Sampler(int count) {
		tick = new AveragedValue(ArgVerify.greaterThan("count", 0, count));
	}

	/**
	 * Sets this sampler's sample start time to the current system time.
	 */
	public void start() {
		start = System.nanoTime();
	}
	/**
	 * Samples time elapsed since the last invocation of {@link #start()}.
	 */
	public void end() {
		tick.add(System.nanoTime() - start);
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
		return tick.getCount();
	}

	@Override
	public String toString() {
		return "Sampler{" +
				"tick=" + tick +
				", start=" + start +
				'}';
	}
}
