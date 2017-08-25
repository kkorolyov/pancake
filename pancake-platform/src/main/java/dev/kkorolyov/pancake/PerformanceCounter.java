package dev.kkorolyov.pancake;

import dev.kkorolyov.simplelogs.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides global access to performance counts.
 */
public final class PerformanceCounter {
	private static final long maxTime = Long.parseLong(Config.config.get("maxTime"));
	private static final int samples = Math.max(1, Integer.parseInt(Config.config.get("samples")));
	private static final Logger log = Config.getLogger(PerformanceCounter.class);

	private static final Map<GameSystem, Usage> systemUsage = new LinkedHashMap<>();
	private static long start;

	private PerformanceCounter() {}

	/**
	 * Sets the shared time counter to the current system time.
	 */
	public static void start() {
		start = System.nanoTime();
	}
	/**
	 * Sets the usage time of {@code system} as μs since the last invocation of {@link #start()} and resets the shared time counter.
	 * @param system game system to set usage of
	 */
	public static void end(GameSystem system) {
		systemUsage.computeIfAbsent(system, k -> new Usage(system, samples))
				.add((System.nanoTime() - start) / 1000);
	}

	/** @return all system usage average values */
	public static Iterable<Usage> usages() {
		return systemUsage.values();
	}

	/**
	 * Tracks the average time usage of a game system.
	 */
	public static class Usage {
		private final GameSystem system;
		private final String systemName;

		private final long[] samples;
		private int counter;
		private long value;

		private Usage(GameSystem system, int samples) {
			this.system = system;
			this.systemName = system.getClass().getSimpleName();
			this.samples = new long[samples];
		}

		private void add(long sample) {
			samples[counter++] = sample;

			if (counter >= samples.length) {
				value = 0;
				for (long s : samples) value += s;
				value /= samples.length;

				counter = 0;
			}
			if (exceedsMax()) {
				log.warning("{} exceeded max usage by {} microseconds",
						system, value - maxTime, maxTime, value);
			}
		}

		/** @return associated game system */
		public GameSystem getSystem() {
			return system;
		}

		/** @return average time usage value in μs */
		public long getValue() {
			return value;
		}

		/** @return {@code true} if this average value exceeds the expected maximum value */
		public boolean exceedsMax() {
			return value > maxTime;
		}

		/** @return usage representation in the format: {@code {systemName}: {value}μs} */
		@Override
		public String toString() {
			return systemName + ": " + value + "\u00b5s";
		}
	}
}
