package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.event.EventBroadcaster;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.media.Camera;
import dev.kkorolyov.pancake.platform.media.ImageRegistry;
import dev.kkorolyov.pancake.platform.media.SoundRegistry;

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

	protected final ImageRegistry images = new ImageRegistry();
	protected final SoundRegistry sounds = new SoundRegistry();
	protected final ActionRegistry actions = new ActionRegistry();

	protected final EventBroadcaster events = new EventBroadcaster();
	protected final EntityPool entities = new EntityPool(events);

	protected final GameEngine engine = new GameEngine(events, entities);
	protected final GameLoop gameLoop = new GameLoop(engine);

	protected Launcher() {
		LauncherConfig config = config();
		config.verify();

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
		events.enqueue(event, object);
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
