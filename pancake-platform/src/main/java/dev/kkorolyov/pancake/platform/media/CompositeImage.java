package dev.kkorolyov.pancake.platform.media;

import dev.kkorolyov.pancake.platform.math.Vector;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * An image formed by layering a collection of base images.
 */
public class CompositeImage implements Iterable<Image> {
	private final List<Image> images = new ArrayList<>();
	private final Vector size = new Vector();

	/**
	 * Constructs a new composite image.
	 * @param images images forming final image, in order of first to last rendered
	 */
	public CompositeImage(Image... images) {
		this(Arrays.asList(images));
	}
	/**
	 * Constructs a new composite image.
	 * @param images images forming final image, in order of first to last rendered
	 */
	public CompositeImage(Iterable<Image> images) {
		for (Image image : images) add(image);
	}

	/**
	 * Adds all images from a composite image to the composition.
	 * @param image composition containing images to add
	 */
	public void add(CompositeImage image) {
		for (Image base : image) add(base);
	}
	/**
	 * Adds an image to the top of the composition.
	 * @param image added image
	 */
	public void add(Image image) {
		images.add(image);

		if (image.getWidth() > size.getX()) size.setX((float) image.getWidth());
		if (image.getHeight() > size.getY()) size.setY((float) image.getHeight());
	}
	/**
	 * Removes an image from the composition.
	 * @param image removed image
	 * @return {@code true} if the composition contained {@code image} and it was removed
	 */
	public boolean remove(Image image) {
		return images.remove(image);
	}

	/**
	 * Clears all images.
	 * @return number of cleared images
	 */
	public int clear() {
		int size = images.size();
		images.clear();
		return size;
	}

	/** @return vector defining minimum width and height in px encompassing all images in the composition */
	public Vector getSize() {
		return size;
	}

	/** @return iterator over all base images in render order */
	@Override
	public Iterator<Image> iterator() {
		return images.iterator();
	}
}
