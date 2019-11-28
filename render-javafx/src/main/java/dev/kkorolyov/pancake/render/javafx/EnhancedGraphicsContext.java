package dev.kkorolyov.pancake.render.javafx;

import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform;
import dev.kkorolyov.pancake.platform.media.graphic.shape.Shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * A {@link GraphicsContext} wrapper providing additional stateful functionality.
 * Clients must apply stateful augmentations to the wrapped graphics context solely through this interface.
 * These augmentations are retained only until the next {@link #get()}, ensuring subsequent clients are unaffected by unexpected augmentations.
 */
public class EnhancedGraphicsContext {
	private final GraphicsContext g;

	private final Rotate rotate = new Rotate();

	private boolean resetRotate;
	private boolean resetFill;
	private boolean resetStroke;
	private boolean resetLineWidth;

	/**
	 * Constructs a new enhanced graphics context.
	 * @param g wrapped graphics context
	 */
	public EnhancedGraphicsContext(GraphicsContext g) {
		this.g = g;
	}

	/**
	 * @param shape shape with properties to apply
	 * @return {@code this} after applying shape properties
	 */
	public EnhancedGraphicsContext shape(Shape<?> shape) {
		return fill(shape.getFill())
				.stroke(shape.getStroke())
				.strokeWidth(shape.getStrokeWidth());
	}

	/**
	 * @param color fill color to set
	 * @return {@code this} after applying fill
	 */
	public EnhancedGraphicsContext fill(Shape.Color color) {
		if (color != null) {
			g.setFill(Color.color(color.red, color.green, color.blue, color.alpha));

			resetFill = false;
		}
		return this;
	}

	/**
	 * @param color stroke color to set
	 * @return {@code this} after applying stroke
	 */
	public EnhancedGraphicsContext stroke(Shape.Color color) {
		if (color != null) {
			g.setStroke(Color.color(color.red, color.green, color.blue, color.alpha));

			resetStroke = false;
		}
		return this;
	}

	/**
	 * @param strokeWidth stroke width to set
	 * @return {@code this} after applying stroke width
	 */
	public EnhancedGraphicsContext strokeWidth(Double strokeWidth) {
		if (strokeWidth != null) {
			g.setLineWidth(strokeWidth);

			resetLineWidth = false;
		}
		return this;
	}

	/**
	 * @param transform transform describing rotation
	 * @return {@code this} after applying rotation
	 */
	public EnhancedGraphicsContext rotate(RenderTransform transform) {
		rotate(transform.getRotation().getZ(), transform.getPosition());

		resetRotate = false;

		return this;
	}
	private void rotate(double angle, Vector pivot) {
		if (Double.compare(rotate.getAngle(), 0) != 0) {
			rotate.setAngle(-Math.toDegrees(angle));

			if (pivot != null) {
				rotate.setPivotX(pivot.getX());
				rotate.setPivotY(pivot.getY());
			}
			g.setTransform(
					rotate.getMxx(), rotate.getMyx(),
					rotate.getMxy(), rotate.getMyy(),
					rotate.getTx(), rotate.getTy()
			);
		}
	}

	/** @return wrapped graphics context with any augmentations applied for the current client */
	public GraphicsContext get() {
		if (resetRotate) rotate(0, null);
		resetRotate = true;

		if (resetFill) g.setFill(Color.BLACK);
		resetFill = true;

		if (resetStroke) g.setStroke(Color.BLACK);
		resetStroke = true;

		if (resetLineWidth) g.setLineWidth(1);
		resetLineWidth = true;

		return g;
	}
}
