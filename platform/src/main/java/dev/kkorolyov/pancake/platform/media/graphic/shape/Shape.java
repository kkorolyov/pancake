package dev.kkorolyov.pancake.platform.media.graphic.shape;

import dev.kkorolyov.pancake.platform.media.graphic.Renderable;

/**
 * A freeform renderable artifact.
 */
public abstract class Shape implements Renderable {
	private Color fill;
	private Color stroke;
	private Double strokeWidth;

	/** @return inner color */
	public final Color getFill() {
		return fill;
	}
	/** @param fill inner color */
	public final void setFill(Color fill) {
		this.fill = fill;
	}

	/** @return outline color */
	public final Color getStroke() {
		return stroke;
	}
	/** @param stroke outline color */
	public final void setStroke(Color stroke) {
		this.stroke = stroke;
	}

	/** @return outline width in {@code px} */
	public final Double getStrokeWidth() {
		return strokeWidth;
	}
	/** @param strokeWidth outline width in {@code px} */
	public final void setStrokeWidth(Double strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	/**
	 * Defines the sRGB color of rendered shapes.
	 */
	public static final class Color {
		public static final Color BLACK = new Color(0, 0, 0);
		public static final Color GRAY = new Color(150, 150, 150);
		public static final Color WHITE = new Color(255, 255, 255);

		public static final Color RED = new Color(255, 0, 0);
		public static final Color GREEN = new Color(0, 255, 0);
		public static final Color BLUE = new Color(0, 0, 255);

		private final int value;

		/**
		 * Constructs a variant of an {@code other} color with given opacity.
		 * @param other base color
		 * @param alpha new opacity
		 * @see #Color(int, int, int, int)
		 */
		public Color(Color other, int alpha) {
			this(other.getRed(), other.getGreen(), other.getBlue(), alpha);
		}
		/**
		 * Constructs a new color with maximum opacity.
		 * @see #Color(int, int, int, int)
		 */
		public Color(int red, int green, int blue) {
			this(red, green, blue, 255);
		}
		/**
		 * Constructs a new color.
		 * @param red red component; constrained {@code [0,255]}
		 * @param green green component; constrained {@code [0,255]}
		 * @param blue blue component; constrained {@code [0,255]}
		 * @param alpha opacity component; constrained {@code [0,255]}
		 */
		public Color(int red, int green, int blue, int alpha) {
			value = (red & 0xff) | ((green & 0xff) << 8) | ((blue & 0xff) << 16) | ((alpha & 0xff) << 24);
		}

		public int getRed() {
			return value & 0xff;
		}
		public int getGreen() {
			return ((value >> 8) & 0xff);
		}
		public int getBlue() {
			return ((value >> 16) & 0xff);
		}
		public int getAlpha() {
			return ((value >> 24) & 0xff);
		}
	}
}
