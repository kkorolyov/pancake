package dev.kkorolyov.pancake.platform.media.graphic.shape;

/**
 * A string of text.
 */
public abstract class Text extends Shape<Text> {
	private String value;

	/** @return rendered text string */
	public String getValue() {
		return value;
	}
	/**
	 * @param value rendered text string
	 * @return {@code this}
	 */
	public Text setValue(String value) {
		this.value = value;
		return this;
	}
}
