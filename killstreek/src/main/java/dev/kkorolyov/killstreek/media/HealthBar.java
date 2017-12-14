package dev.kkorolyov.killstreek.media;

import dev.kkorolyov.killstreek.component.Health;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.media.Renderable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * A rendered health bar.
 */
public class HealthBar implements Renderable {
	private static final Vector ORIENTATION_OFFSET = new Vector();

	private final Health health;
	private final Vector size;

	/**
	 * Constructs a new health bar.
	 * @param health health rendered by this bar
	 */
	public HealthBar(Health health, Vector size) {
		this.health = health;
		this.size = size;
	}

	@Override
	public void render(GraphicsContext g, Vector position) {
		Paint initialFill = g.getFill();
		Paint initialStroke = g.getStroke();
		double initialWidth = g.getLineWidth();

		float x = position.getX();
		float y = position.getY();
		float w = size.getX() * health.getPercentage();
		float h = size.getY();

		g.setFill(Color.RED);
		g.fillRect(x, y, w, h);

		g.setStroke(Color.DARKGRAY);
		g.setLineWidth(2);
		g.strokeRect(x, y, w, h);

		g.setFill(initialFill);
		g.setStroke(initialStroke);
		g.setLineWidth(initialWidth);
	}

	@Override
	public Vector size() {
		return size;
	}

	@Override
	public Vector getOrientationOffset() {
		return ORIENTATION_OFFSET;
	}
}
