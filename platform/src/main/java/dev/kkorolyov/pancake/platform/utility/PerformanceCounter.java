package dev.kkorolyov.pancake.platform.utility;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.plugin.GameSystem;
import dev.kkorolyov.pancake.platform.math.AveragedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maintains performance counts.
 */
public final class PerformanceCounter {
	private static final Logger log = LoggerFactory.getLogger(PerformanceCounter.class);

	private final long maxTime = Long.parseLong(Config.get().getProperty("maxTime"));
	private final int samples = Math.max(1, Integer.parseInt(Config.get().getProperty("samples")));

	private long last;
	private final AveragedValue tick = new AveragedValue(samples);
	private long start;
	private final Map<GameSystem, Usage> systemUsage = new LinkedHashMap<>();

	/**
	 * Adds a sample to the usage time of an entire tick as ns since the last invocation of this method.
	 */
	public void tick() {
		long now = System.nanoTime();
		tick.add(now - last);
		last = now;
	}

	/**
	 * Sets the system time counter to the current system time.
	 */
	public void start() {
		start = System.nanoTime();
	}
	/**
	 * Adds a sample to the usage time of {@code system} as μs since the last invocation of {@link #start()}.
	 * @param system game system to set usage of
	 */
	public void end(GameSystem system) {
		systemUsage.computeIfAbsent(system, k -> new Usage(system, samples, maxTime))
				.add((System.nanoTime() - start) / 1000);
	}

	/** @return average ticks per second */
	public long getTps() {
		return Math.round(1e9 / tick.get());
	}
	/** @return all system usage average values */
	public Iterable<Usage> getUsages() {
		return systemUsage.values();
	}

	/**
	 * Tracks the average time usage of a game system.
	 */
	public static final class Usage {
		private final String systemName;
		private final AveragedValue value;
		private final long maxTime;

		private Usage(GameSystem system, int samples, long maxTime) {
			systemName = system.getClass().getSimpleName();
			value = new AveragedValue(samples);
			this.maxTime = maxTime;
		}

		private void add(long sample) {
			value.add(sample);

			if (exceedsMax()) {
				log.warn(
						"{} exceeded max usage by {} microseconds",
						systemName,
						value.get() - maxTime
				);
			}
		}

		/** @return average time usage value in μs */
		public long getValue() {
			return value.get();
		}

		/** @return {@code true} if this average value exceeds the expected maximum value */
		public boolean exceedsMax() {
			return value.get() > maxTime;
		}

		/** @return usage representation in the format: {@code {systemName}: {value}μs} */
		@Override
		public String toString() {
			return systemName + ": " + value.get() + "\u00b5s";
		}
	}
}
