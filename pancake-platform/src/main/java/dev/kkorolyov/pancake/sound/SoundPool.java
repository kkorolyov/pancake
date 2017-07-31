package dev.kkorolyov.pancake.sound;

import dev.kkorolyov.pancake.Config;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simpleprops.Properties;

import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Provides access to sounds by name.
 * Names are mapped to an arbitrary-length collection of sound resources.
 * When retrieving from a name with multiple resource associations, a randomly-selected associated resource is returned.
 */
public class SoundPool {
	private static final int DEFAULT_CACHE_SIZE = 15;
	private static final Random rand = new Random();
	private static final Logger log = Config.getLogger(SoundPool.class);

	private final Map<String, List<String>> resourceLists = new HashMap<>();
	private final Map<String, AudioClip> cache;
	private int cacheSize;

	/**
	 * Constructs a new sound pool using the {@link #DEFAULT_CACHE_SIZE}.
	 */
	public SoundPool() {
		this(DEFAULT_CACHE_SIZE);
	}
	/**
	 * Constructs a new sound pool using some cache size.
	 * @param cacheSize maximum size of audio file cache
	 */
	public SoundPool(int cacheSize) {
		cache = new LinkedHashMap<String, AudioClip>(cacheSize) {
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
		List<String> resources = resourceLists.get(name);
		if (resources == null) throw new NoSuchElementException("No resources associated with name: " + name);

		return resources.get(rand.nextInt(resources.size()));
	}

	/**
	 * Parses sounds from a configuration file.
	 * Each entry is a sound name mapped to an arbitrary-length list of resources.
	 * If a resource does not contain {@code //}, it is assumed to be found on the local filesystem.
	 * @param soundConfig sound configuration file
	 */
	public void put(Properties soundConfig) {
		for (String name : soundConfig.keys()) {
			for (String resource : soundConfig.getArray(name)) {
				String url = resource;

				if (!resource.contains("//")) {
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
	 * Adds a {@code name -> resource} association.
	 * @param name sound name
	 * @param resource resource URL
	 */
	public void put(String name, String resource) {
		List<String> resources = resourceLists.computeIfAbsent(name, k -> {
			log.info("No resource list for name={}, creating new...", name);
			return new ArrayList<>();
		});
		resources.add(resource);
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
