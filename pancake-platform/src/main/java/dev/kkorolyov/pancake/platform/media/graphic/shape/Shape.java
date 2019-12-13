package dev.kkorolyov.pancake.platform.media.graphic.shape;

import dev.kkorolyov.pancake.platform.media.graphic.Renderable;

/**
 * A freeform renderable artifact.
 * @param <T> shape type
 */
public abstract class Shape<T extends Shape> implements Renderable {
	private Color fill;
	private Color stroke;
	private Double strokeWidth;

	/** @return inner color */
	public Color getFill() {
		return fill;
	}
	/** @param fill inner color */
	public T setFill(Color fill) {
		this.fill = fill;
		return (T) this;
	}

	/** @return outline color */
	public Color getStroke() {
		return stroke;
	}
	/** @param stroke outline color */
	public T setStroke(Color stroke) {
		this.stroke = stroke;
		return (T) this;
	}

	/** @return outline width in {@code px} */
	public Double getStrokeWidth() {
		return strokeWidth;
	}
	public T setStrokeWidth(Double strokeWidth) {
		this.strokeWidth = strokeWidth;
		return (T) this;
	}

	/**
	 * Defines the sRGB color of rendered shapes.
	 */
	public static final class Color {
		public static final Color BLACK = new Color((short) 0, (short) 0, (short) 0);
		public static final Color GRAY = new Color((short) 150, (short) 150, (short) 150);
		public static final Color RED = new Color((short) 255, (short) 0, (short) 0);

		public final short red;
		public final short green;
		public final short blue;
		public final double alpha;

		/**
		 * Constructs a variant of an {@code other} color with given opacity.
		 * @param other base color
		 * @param alpha new opacity
		 * @see #Color(short, short, short, double)
		 */
		public Color(Color other, double alpha) {
			this(other.red, other.green, other.blue, alpha);
		}
		/**
		 * Constructs a new color with maximum opacity.
		 * @see #Color(short, short, short, double)
		 */
		public Color(short red, short green, short blue) {
			this(red, green, blue, 1.0);
		}
		/**
		 * Constructs a new color.
		 * @param red red component; constrained {@code [0,255]}
		 * @param green green component; constrained {@code [0,255]}
		 * @param blue blue component; constrained {@code [0,255]}
		 * @param alpha opacity component; constrained {@code [0,1]}
		 */
		public Color(short red, short green, short blue, double alpha) {
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.alpha = alpha;
		}
	}
}
