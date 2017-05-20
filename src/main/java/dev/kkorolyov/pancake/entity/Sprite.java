package dev.kkorolyov.pancake.entity;

import javafx.scene.image.Image;

/**
 * A dynamic image.
 */
public class Sprite {
	private final Image baseImage;
	
	/**
	 * Constructs a new sprite.
	 * @param baseImage base sprite image
	 */
	public Sprite(Image baseImage) {
		this.baseImage = baseImage;
	}
	
	public Image getImage() {
		return baseImage;
	}
}
