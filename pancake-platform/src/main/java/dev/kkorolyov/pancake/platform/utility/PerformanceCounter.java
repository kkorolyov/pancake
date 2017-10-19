package dev.kkorolyov.pancake.platform.utility;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.simplelogs.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maintains performance counts.
 */
public class PerformanceCounter {
	private static final long MAX_TIME = Long.parseLong(Config.config.get("maxTime"));
	private static final int SAMPLES = Math.max(1, Integer.parseInt(Config.config.get("samples")));
	private static final Logger log = Config.getLogger(PerformanceCounter.class);

	private final Map<GameSystem, Usage> systemUsage = new LinkedHashMap<>();
	private long start;

	/**
	 * Sets the shared time counter to the current system time.
	 */
	public void start() {
		start = System.nanoTime();
	}
	/**
	 * Sets the usage time of {@code system} as μs since the last invocation of {@link #start()} and resets the shared time counter.
	 * @param system game system to set usage of
	 */
	public void end(GameSystem system) {
		systemUsage.computeIfAbsent(system, k -> new Usage(system, SAMPLES))
				.add((System.nanoTime() - start) / 1000);
	}

	/** @return all system usage average values */
	public Iterable<Usage> usages() {
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
						system, value - MAX_TIME, MAX_TIME, value);
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
			return value > MAX_TIME;
		}

		/** @return usage representation in the format: {@code {systemName}: {value}μs} */
		@Override
		public String toString() {
			return systemName + ": " + value + "\u00b5s";
		}
	}
}
