package dev.kkorolyov.pancake;

import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.entity.EntityPool;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.graphics.Camera;
import dev.kkorolyov.pancake.graphics.ImagePool;
import dev.kkorolyov.pancake.input.ActionPool;
import dev.kkorolyov.pancake.math.Vector;

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
	private GameEngine engine;
	private GameLoop gameLoop;

	protected final String title;
	protected final Canvas canvas = new Canvas();
	protected final Scene scene = new Scene(new Group(canvas));

	protected final Camera camera = new Camera(new Vector(), null, 0, 0);

	protected final ImagePool images = new ImagePool();
	protected final ActionPool actions = new ActionPool();

	/**
	 * Constructs a new launcher using a config.
	 * @param config initial configuration
	 * @throws IllegalStateException if not all required fields in {@code config} are set
	 */
	protected Launcher(LauncherConfig config) {
		config.verify();

		title = config.title;
		setSize(config.size.getX(), config.size.getY());
		camera.setUnitPixels(config.unitPixels);
	}

	/**
	 * Initializes the image pool.
	 * @param images empty image pool
	 */
	protected abstract void initImages(ImagePool images);
	// /**
	//  * Initializes the sound pool.
	//  * @param sounds empty sound pool
	//  */
	// protected abstract void initSounds(SoundPool sounds);
	/**
	 * Initializes the action pool.
	 * @param actions empty action pool
	 */
	protected abstract void initActions(ActionPool actions);

	/**
	 * Initializes the entity pool.
	 * @param entities empty entity pool
	 */
	protected abstract void initEntities(EntityPool entities);

	/** @return all components used in game */
	protected abstract Iterable<Class<? extends Component>> components();
	/** @return all systems used in game, in update order */
	protected abstract Iterable<GameSystem> systems();

	private void configureStage(Stage stage) {
		stage.setTitle(title);
		stage.setScene(scene);
		stage.show();

		stage.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
				setSize((float) (canvas.getWidth() + newValue.doubleValue() - oldValue.doubleValue()), (float) canvas.getHeight()));
		stage.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
				setSize((float) canvas.getWidth(), (float) (canvas.getHeight() + newValue.doubleValue() - oldValue.doubleValue())));
	}
	private void setSize(float width, float height) {
		canvas.setWidth(width);
		canvas.setHeight(height);

		camera.setSize(width, height);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Signature.index(components());

		engine = new GameEngine(systems());
		gameLoop = new GameLoop(engine);

		initImages(images);
		initActions(actions);
		initEntities(engine.getEntities());

		gameLoop.start();

		configureStage(primaryStage);
	}

	protected static class LauncherConfig {
		private String title;
		private Vector size;
		private Vector unitPixels;

		protected LauncherConfig title(String title) {
			this.title = title;
			return this;
		}

		protected LauncherConfig size(Vector size) {
			this.size = size;
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
