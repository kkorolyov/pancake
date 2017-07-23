package dev.kkorolyov.pancake;

import dev.kkorolyov.pancake.component.Bounds;
import dev.kkorolyov.pancake.component.Chain;
import dev.kkorolyov.pancake.component.Input;
import dev.kkorolyov.pancake.component.Spawner;
import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.component.movement.Damping;
import dev.kkorolyov.pancake.component.movement.Force;
import dev.kkorolyov.pancake.component.movement.MaxSpeed;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.entity.EntityPool;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.graphics.Camera;
import dev.kkorolyov.pancake.graphics.ImagePool;
import dev.kkorolyov.pancake.input.Action;
import dev.kkorolyov.pancake.input.ActionPool;
import dev.kkorolyov.pancake.math.Vector;
import dev.kkorolyov.pancake.math.WeightedDistribution;
import dev.kkorolyov.pancake.system.AccelerationSystem;
import dev.kkorolyov.pancake.system.ChainSystem;
import dev.kkorolyov.pancake.system.CollisionSystem;
import dev.kkorolyov.pancake.system.DampingSystem;
import dev.kkorolyov.pancake.system.InputSystem;
import dev.kkorolyov.pancake.system.MovementSystem;
import dev.kkorolyov.pancake.system.RenderSystem;
import dev.kkorolyov.pancake.system.SpawnSystem;
import dev.kkorolyov.pancake.system.SpeedCapSystem;
import dev.kkorolyov.simpleprops.Properties;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

public class Interactive extends Application {
	private static final float MOVE_FORCE = 500;
	private static final float OBJECT_MASS = .1f;
	private static final float PLAYER_MASS = 10;
	private static final float OBJECT_DAMPING = .9f;
	private static final float PLAYER_DAMPING = .5f;
	private static final Vector BOX = new Vector(1, 1);
	private static final float RADIUS = BOX.getX() / 2;

	private static final Random rand = new Random();

	private final Canvas canvas = new Canvas(560, 560);
	private final Scene scene = new Scene(new Group(canvas));

	private final ActionPool actions = buildActionPool();
	private final ImagePool images = buildImagePool();

	private final Camera camera = new Camera(new Vector(), new Vector(64, -64, 1), (float) canvas.getWidth(), (float) canvas.getHeight());
	private final Vector cursor = new Vector();

	public static void main(String[] args) throws URISyntaxException {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		tweakStage(primaryStage);

		Signature.index(Bounds.class,
				Damping.class,
				Force.class,
				MaxSpeed.class,
				Sprite.class,
				Transform.class,
				Chain.class,
				Velocity.class,
				Input.class,
				Spawner.class);
		GameEngine engine = new GameEngine(
				new InputSystem(scene, camera, cursor),
				new DampingSystem(),
				new AccelerationSystem(),
				new SpeedCapSystem(),
				new MovementSystem(),
				new ChainSystem(),
				new CollisionSystem(),
				new SpawnSystem(),
				new RenderSystem(canvas, camera));
		new GameLoop(engine).start();

		EntityPool entities = engine.getEntities();

		// Wall
		entities.create(new Transform(new Vector(-3, 0), randRotation()),
				new Bounds(new Vector(5, 21)),
				new Sprite(images.get("box")));

		addGround(entities);
		addBoxes(entities, 200);
		addSpheres(entities, 200);
		addPlayer(entities);
	}
	private void tweakStage(Stage stage) {
		stage.setTitle("Pancake: Interactive Test");
		stage.setScene(scene);
		stage.show();

		stage.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			canvas.setWidth(canvas.getWidth() + newValue.doubleValue() - oldValue.doubleValue());
			camera.setSize((float) canvas.getWidth(), (float) canvas.getHeight());
		});
		stage.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			canvas.setHeight(canvas.getHeight() + newValue.doubleValue() - oldValue.doubleValue());
			camera.setSize((float) canvas.getWidth(), (float) canvas.getHeight());
		});
	}

	private ActionPool buildActionPool() {
		ActionPool actions = new ActionPool();

		actions.put(new Action("FORCE_UP", e -> e.get(Force.class).getForce().translate(0, MOVE_FORCE)));
		actions.put(new Action("FORCE_DOWN", e -> e.get(Force.class).getForce().translate(0, -MOVE_FORCE)));
		actions.put(new Action("FORCE_RIGHT", e -> e.get(Force.class).getForce().translate(MOVE_FORCE, 0)));
		actions.put(new Action("FORCE_LEFT", e -> e.get(Force.class).getForce().translate(-MOVE_FORCE, 0)));
		actions.put(new Action("RESET", e -> e.get(Transform.class).getPosition().set(0, 0)));

		try {
			actions.put(new Properties(Paths.get(ClassLoader.getSystemResource("config/actions").toURI())));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return actions;
	}

	private ImagePool buildImagePool() {
		ImagePool images = new ImagePool();

		try {
			images.put(new Properties(Paths.get(ClassLoader.getSystemResource("config/images").toURI())));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return images;
	}

	private void addGround(EntityPool entities) {
		Sprite ground = new Sprite(images.get("ground"), 3, 2, 0);
		for (int i = -15; i <= 15; i+= 2) {
			for (int j = -15; j <= 15; j+= 2) {
				entities.create(new Transform(new Vector(i, j, -1)),
						ground);
			}
		}
	}

	private void addPlayer(EntityPool entities) throws Exception {
		Transform playerTransform = new Transform(new Vector(), randRotation());
		entities.create(playerTransform,
				new Velocity(),
				new Force(PLAYER_MASS),
				new Damping(PLAYER_DAMPING),
				new Bounds(BOX, RADIUS),
				new Sprite(images.get("player"), 4, 3, 1 / 60f),
				// buildSpawner(),
				new Input(true, actions.parseConfig(new Properties(Paths.get(ClassLoader.getSystemResource("config/keys").toURI())))));

		entities.create(new Transform(camera.getPosition()),	// Chained camera
				new Chain(playerTransform.getPosition(), 1));
	}

	private void addSpheres(EntityPool entities, int num) {
		int line = (int) Math.sqrt(num);

		for (int i = 1; i <= line; i++) {
			for (int j = 1; j <= line; j++) {
				addObject(entities, new Vector(i, j), new Bounds(RADIUS), new Sprite(images.get("sphere")));
			}
		}
	}
	private void addBoxes(EntityPool entities, int num) {
		int line = (int) Math.sqrt(num);

		for (int i = 1; i <= line; i++) {
			for (int j = 1; j <= line; j++) {
				addObject(entities, new Vector(i, -j), new Bounds(BOX), new Sprite(images.get("box")));
			}
		}
	}

	private void addObject(EntityPool entities, Vector position, Bounds bounds, Sprite sprite) {
		entities.create(new Transform(position, randRotation()),
				new Velocity(),
				new Force(OBJECT_MASS),
				new Damping(OBJECT_DAMPING),
				bounds,
				sprite);
	}

	private Spawner buildSpawner() {
		return new Spawner(1, 4, .1f, new WeightedDistribution<Supplier<Iterable<Component>>>()
				.add(1, () -> Arrays.asList(new Transform(new Vector(1, 1), randRotation()),
						new Velocity(),
						new Force(OBJECT_MASS),
						new Damping(OBJECT_DAMPING),
						new Bounds(BOX, RADIUS),
						new Sprite(images.get("sphere")))));
	}

	private float randRotation() {
		return rand.nextInt(360);
	}
}
