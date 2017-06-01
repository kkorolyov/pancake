package dev.kkorolyov.pancake.component;

import dev.kkorolyov.pancake.core.Component;
import javafx.scene.image.Image;

/**
 * A dynamic image.
 */
public class Sprite implements Component {
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
