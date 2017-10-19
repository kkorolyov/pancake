package dev.kkorolyov.pancake.platform.media;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simpleprops.Properties;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

import static dev.kkorolyov.pancake.platform.Resources.in;

/**
 * A collection of composite images retrievable by name.
 */
public class ImageRegistry {
	private static final Logger log = Config.getLogger(ImageRegistry.class);

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
	 * @param path path to image configuration file
	 */
	// TODO Use serializers instead
	public void put(String path) {
		Properties imageConfig = new Properties(in(path));

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
