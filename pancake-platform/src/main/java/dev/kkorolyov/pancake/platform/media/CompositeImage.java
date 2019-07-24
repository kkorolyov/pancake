package dev.kkorolyov.pancake.platform.media;

import dev.kkorolyov.pancake.platform.media.graphic.Image;
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform;

import java.util.ArrayList;
import java.util.List;

import static dev.kkorolyov.simplefuncs.stream.Iterables.append;

/**
 * An image formed by layering a collection of base images.
 */
public class CompositeImage extends Image {
	private final List<Image> images = new ArrayList<>();

	/**
	 * Constructs a new composite image.
	 * @see #CompositeImage(Iterable)
	 */
	public CompositeImage(Image image, Image... images) {
		this(append(image, images));
	}
	/**
	 * Constructs a new composite image.
	 * @param images images forming final image, in order of first to last rendered
	 */
	public CompositeImage(Iterable<Image> images) {
		images.forEach(this::add);
	}

	/**
	 * Adds an image to the top of the composition.
	 * @param image added image
	 */
	public void add(Image image) {
		images.add(image);

		getSize().set(
				Math.max(getSize().getX(), image.getSize().getX()),
				Math.max(getSize().getY(), image.getSize().getY())
		);
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

	@Override
	public void render(RenderTransform transform) {
		for (Image image : images) {
			image.render(transform);
		}
	}
}
