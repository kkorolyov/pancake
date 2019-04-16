package dev.kkorolyov.pancake.platform.media;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simpleprops.Properties;
import dev.kkorolyov.simplestructs.WeightedDistribution;

import javafx.scene.media.Media;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import static dev.kkorolyov.pancake.platform.Resources.in;

/**
 * A collection of audio resources retrievable by name.
 * Names are mapped to an arbitrary-length collection of audio resources.
 * When retrieving from a name with multiple resource associations, a randomly-selected associated resource is returned.
 */
public class AudioRegistry {
	private static final int DEFAULT_CACHE_SIZE = 15;
	private static final Logger log = Config.getLogger(AudioRegistry.class);

	private final Map<String, WeightedDistribution<String>> resourceDistributions = new HashMap<>();
	private final Map<String, Media> cache;
	private int cacheSize;

	/**
	 * Constructs a new audio registry using the {@link #DEFAULT_CACHE_SIZE}.
	 */
	public AudioRegistry() {
		this(DEFAULT_CACHE_SIZE);
	}
	/**
	 * Constructs a new audio registry using some cache size.
	 * @param cacheSize maximum size of audio file cache
	 */
	public AudioRegistry(int cacheSize) {
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
	 * Retrieves an audio resource randomly selected from all resources associated with a name.
	 * @param name sound name
	 * @return randomly-selected sound associated with {@code name}
	 * @throws NoSuchElementException if no audio associated with {@code name}
	 */
	public Media get(String name) {
		return cache.computeIfAbsent(getResource(name), Media::new);
	}
	private String getResource(String name) {
		WeightedDistribution<String> resources = resourceDistributions.get(name);
		if (resources == null) throw new NoSuchElementException("No resources associated with name: " + name);

		return resources.get();
	}

	/**
	 * Parses audio from a configuration file.
	 * Each entry is a clip name mapped to an arbitrary-length list of resources, all with weight {@code 1}.
	 * If a resource does not contain {@code //}, it is assumed to be found on the local filesystem.
	 * @param path path to audio configuration file
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
	 * @param name audio resource name
	 * @param resource resource URL
	 * @param weight {@code resource} weight compared to other resources in the same distribution
	 */
	public void put(String name, String resource, int weight) {
		resourceDistributions.computeIfAbsent(name, k -> {
			log.info("No resource list for name={}, creating new...", name);
			return new WeightedDistribution<>();
		}).add(resource, weight);
	}

	/** @return maximum size of audio file cache */
	public int getCacheSize() {
		return cacheSize;
	}
	/**
	 * Constrained {@code >= 0}.
	 * This method also clears the current audio file cache.
	 * @param cacheSize new audio file cache maximum size
	 */
	public void setCacheSize(int cacheSize) {
		this.cacheSize = Math.max(cacheSize, 0);
		cache.clear();
	}
}
