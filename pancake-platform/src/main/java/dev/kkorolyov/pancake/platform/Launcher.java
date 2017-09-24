package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.graphics.Camera;
import dev.kkorolyov.pancake.platform.graphics.ImagePool;
import dev.kkorolyov.pancake.platform.input.ActionPool;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.sound.SoundPool;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static dev.kkorolyov.pancake.platform.event.Events.CAMERA_CREATED;
import static dev.kkorolyov.pancake.platform.event.Events.CANVAS_CREATED;
import static dev.kkorolyov.pancake.platform.event.Events.SCENE_CREATED;

/**
 * Provides a framework to configure and launch Pancake games.
 */
public abstract class Launcher extends Application {
	protected final String title;
	protected final Canvas canvas;
	protected final Scene scene;

	protected final Camera camera;

	protected final ImagePool images = new ImagePool();
	protected final SoundPool sounds = new SoundPool();
	protected final ActionPool actions = new ActionPool();

	protected final GameEngine engine;
	protected final GameLoop gameLoop;

	protected Launcher() {
		LauncherConfig config = config();
		config.verify();

		Signature.index();
		engine = new GameEngine();
		gameLoop = new GameLoop(engine);

		canvas = announce(new Canvas(), CANVAS_CREATED);
		scene = announce(new Scene(new Group(canvas)), SCENE_CREATED);
		camera = announce(new Camera(new Vector(), config.unitPixels, 0, 0), CAMERA_CREATED);

		title = config.title;
		setSize(config.size.getX(), config.size.getY());
	}

	/** @return configuration defining basic properties */
	protected abstract LauncherConfig config();

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
		primaryStage.getIcons().add(new Image("pancake-icon.png"));
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

	private <T> T announce(T object, String event) {
		engine.getEvents().enqueue(event, object);
		return object;
	}

	/**
	 * A builder of basic configuration options.
	 */
	protected static class LauncherConfig {
		private String title;
		private Vector size;
		private Vector unitPixels;

		/**
		 * Constructs a new launcher configuration.
		 */
		public LauncherConfig() {
			// Allow instantiation by subclasses
		}

		public LauncherConfig title(String title) {
			this.title = title;
			return this;
		}

		public LauncherConfig size(float width, float height) {
			this.size = new Vector(width, height);
			return this;
		}

		public LauncherConfig unitPixels(Vector unitPixels) {
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
