package dev.kkorolyov.pancake.platform.media;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.math.WeightedDistribution;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simpleprops.Properties;

import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import static dev.kkorolyov.pancake.platform.Resources.in;

/**
 * A collection of sounds retrievable by name.
 * Names are mapped to an arbitrary-length collection of sound resources.
 * When retrieving from a name with multiple resource associations, a randomly-selected associated resource is returned.
 */
public class SoundRegistry {
	private static final int DEFAULT_CACHE_SIZE = 15;
	private static final Logger log = Config.getLogger(SoundRegistry.class);

	private final Map<String, WeightedDistribution<String>> resourceDistributions = new HashMap<>();
	private final Map<String, AudioClip> cache;
	private int cacheSize;

	/**
	 * Constructs a new sound pool using the {@link #DEFAULT_CACHE_SIZE}.
	 */
	public SoundRegistry() {
		this(DEFAULT_CACHE_SIZE);
	}
	/**
	 * Constructs a new sound pool using some cache size.
	 * @param cacheSize maximum size of audio file cache
	 */
	public SoundRegistry(int cacheSize) {
		cache = new LinkedHashMap<>(cacheSize) {
			private static final long serialVersionUID = -4374897957713259831L;

			@Override
			protected boolean removeEldestEntry(Entry eldest) {
				return size() > getCacheSize();
			}
		};
		setCacheSize(cacheSize);

		log.debug("Constructed a new SoundPool with cacheSize={}", getCacheSize());
	}

	/**
	 * Retrieves a sound randomly selected from all resources associated with a name.
	 * @param name sound name
	 * @return randomly-selected sound associated with {@code name}
	 * @throws NoSuchElementException if no sounds associated with {@code name}
	 */
	public AudioClip get(String name) {
		return cache.computeIfAbsent(getResource(name), AudioClip::new);
	}
	private String getResource(String name) {
		WeightedDistribution<String> resources = resourceDistributions.get(name);
		if (resources == null) throw new NoSuchElementException("No resources associated with name: " + name);

		return resources.get();
	}

	/**
	 * Parses sounds from a configuration file.
	 * Each entry is a sound name mapped to an arbitrary-length list of resources, all with weight {@code 1}.
	 * If a resource does not contain {@code //}, it is assumed to be found on the local filesystem.
	 * @param path path to sound configuration file
	 */
	// TODO Use serializers instead
	public void put(String path) {
		Properties soundConfig = new Properties(in(path));

		for (String name : soundConfig.keys()) {
			for (String resource : soundConfig.getArray(name)) {
				String url = resource;

				if (!resource.contains("//")) {
					// TODO Currently, only tries classpath
					URL systemUrl = ClassLoader.getSystemResource(resource);
					if (systemUrl == null) throw new NoSuchElementException("No such system resource: " + resource);

					url = systemUrl.toExternalForm();
				}
				put(name, url);
			}
			log.info("Parsed sound entry: {}", name);
		}
	}

	/**
	 * Adds a {@code name -> resource} association with weight {@code 1}.
	 * @param name sound name
	 * @param resource resource URL
	 */
	public void put(String name, String resource) {
		put(name, resource, 1);
	}
	/**
	 * Adds a {@code name -> resource} association.
	 * @param name sound name
	 * @param resource resource URL
	 * @param weight {@code resource} weight compared to other resources in the same distribution
	 */
	public void put(String name, String resource, int weight) {
		WeightedDistribution<String> resources = resourceDistributions.computeIfAbsent(name, k -> {
			log.info("No resource list for name={}, creating new...", name);
			return new WeightedDistribution<>();
		});
		resources.add(weight, resource);
	}

	/** @return maximum size of audio file cache */
	public int getCacheSize() {
		return cacheSize;
	}
	/**
	 * Constrained {@code >= 0}.
	 * This method also clears the current audio clip cache.
	 * @param cacheSize new audio clip cache maximum size
	 */
	public void setCacheSize(int cacheSize) {
		this.cacheSize = Math.abs(cacheSize);
		cache.clear();
	}
}
