package dev.kkorolyov.pancake.graphics;

import java.util.HashMap;
import java.util.Map;

import dev.kkorolyov.pancake.Config;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simpleprops.Properties;
import javafx.scene.image.Image;

/**
 * Maintains a collection of composite images retrievable by name.
 */
public class ImagePool {
	private static final Logger log = Config.getLogger(ImagePool.class);

	private final Map<String, CompositeImage> images = new HashMap<>();

	/**
	 * Retrieves an image by name.
	 * @param name image identifier
	 * @return image mapped to {@code name}, or {@code null} if no such image
	 */
	public CompositeImage get(String name) {
		return images.get(name);
	}

	/**
	 * Parses images from a configuration file.
	 * Each entry is an image name mapped to an arbitrary-length array of base images.
	 * Each base image is first parsed as the name of an image found in this pool, or a filename of an image on the file system if no such image exists in this pool.
	 * @param imageConfig image configuration file
	 */
	public void put(Properties imageConfig) {
		for (String name : imageConfig.keys()) {
			CompositeImage image = new CompositeImage();

			for (String base : imageConfig.getArray(name)) {
				CompositeImage other = get(base);

				if (other != null) image.add(other);
				else image.add(new Image(base));
			}
			put(name, image);
			log.info("Parsed image entry: {}={}", name, image);
		}
	}
	public void put(String name, CompositeImage image) {
		images.put(name, image);
	}
}
