package dev.kkorolyov.pancake.platform.media.graphic.shape;

/**
 * A string of text.
 */
public abstract class Text extends Shape {
	private String value;

	/** @return rendered text string */
	public final String getValue() {
		return value;
	}
	/** @param value rendered text string */
	public final void setValue(String value) {
		this.value = value;
	}
}
