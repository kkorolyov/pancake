package dev.kkorolyov.pancake;

import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.graphics.Camera;
import dev.kkorolyov.pancake.graphics.ImagePool;
import dev.kkorolyov.pancake.input.ActionPool;
import dev.kkorolyov.pancake.math.Vector;
import dev.kkorolyov.pancake.sound.SoundPool;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

/**
 * Provides a framework to configure and launch Pancake games.
 */
public abstract class Launcher extends Application {
	protected final String title;
	protected final Canvas canvas = new Canvas();
	protected final Scene scene = new Scene(new Group(canvas));

	protected final Camera camera = new Camera(new Vector(), null, 0, 0);

	protected final ImagePool images = new ImagePool();
	protected final SoundPool sounds = new SoundPool();
	protected final ActionPool actions = new ActionPool();

	protected final GameEngine engine;
	protected final GameLoop gameLoop;

	protected Launcher() {
		LauncherConfig config = config();
		config.verify();

		title = config.title;
		setSize(config.size.getX(), config.size.getY());
		camera.setUnitPixels(config.unitPixels);

		Signature.index(components());
		engine = new GameEngine(systems());
		gameLoop = new GameLoop(engine);
	}

	/** @return configuration defining basic properties */
	protected abstract LauncherConfig config();

	/** @return all components used in game */
	protected abstract Iterable<Class<? extends Component>> components();
	/** @return all systems used in game, in update order */
	protected abstract Iterable<GameSystem> systems();

	/**
	 * All initialization code should go here.
	 * Examples:
	 * <pre>
	 *   - Image/Action pool setup
	 *   - Attaching event handlers
	 *   - Configuration file application
	 * </pre>
	 */
	public abstract void init() throws Exception;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
				setSize((float) (canvas.getWidth() + newValue.doubleValue() - oldValue.doubleValue()), (float) canvas.getHeight()));
		primaryStage.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
				setSize((float) canvas.getWidth(), (float) (canvas.getHeight() + newValue.doubleValue() - oldValue.doubleValue())));

		gameLoop.start();
	}
	private void setSize(float width, float height) {
		canvas.setWidth(width);
		canvas.setHeight(height);

		camera.setSize(width, height);
	}

	/**
	 * A builder of basic configuration options.
	 */
	protected static class LauncherConfig {
		private String title;
		private Vector size;
		private Vector unitPixels;

		protected LauncherConfig title(String title) {
			this.title = title;
			return this;
		}

		protected LauncherConfig size(float width, float height) {
			this.size = new Vector(width, height);
			return this;
		}

		protected LauncherConfig unitPixels(Vector unitPixels) {
			this.unitPixels = unitPixels;
			return this;
		}

		private void verify() {
			if (title == null) fail("title");
			if (size == null) fail("size");
			if (unitPixels == null) fail("unitPixels");
		}
		private void fail(String field) {
			throw new IllegalStateException("Missing required field: " + field);
		}
	}
}
