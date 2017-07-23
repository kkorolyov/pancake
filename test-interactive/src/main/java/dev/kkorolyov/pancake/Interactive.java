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
import java.util.function.Supplier;

public class Interactive extends Application {
	private static final float MOVE_FORCE = 500;

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

		Sprite playerSprite = new Sprite(images.get("player"), 4, 3, 1 / 60f);
		Sprite sphereSprite = new Sprite(images.get("sphere"));
		Sprite boxSprite = new Sprite(images.get("box"));

		Bounds bounds = new Bounds(new Vector(1, 1, 0), .5f);
		Bounds boxBounds = new Bounds(bounds.getBox());
		Bounds sphereBounds = new Bounds(bounds.getRadius());

		Sprite ground = new Sprite(images.get("ground"), 3, 2, 0);
		for (int i = -15; i <= 15; i+= 2) {
			for (int j = -15; j <= 15; j+= 2) {
				entities.create(new Transform(new Vector(i, j, -1)),
												ground);
			}
		}

		for (int i = 1; i <= 10; i++) {	// Boxes
			for (int j = 1; j <= 10; j++) {
				entities.create(new Transform(new Vector(i, -j)),
												new Velocity(),
												new Force(.1f),
												new Damping(.9f),
												boxBounds,
												boxSprite);
			}
		}
		for (int i = 1; i <= 10; i++) {	// Spheres
			for (int j = 1; j <= 10; j++) {
				entities.create(new Transform(new Vector(i, j)),
												new Velocity(),
												new Force(.1f),
												new Damping(.9f),
												sphereBounds,
												sphereSprite);
			}
		}

		// Wall
		entities.create(new Transform(new Vector(-3, 0)),
										new Bounds(new Vector(5, 21)),
										boxSprite);

		WeightedDistribution<Supplier<Iterable<Component>>> spawnSupply = new WeightedDistribution<>();
		spawnSupply.add(1, () -> Arrays.asList(new Transform(new Vector(1, 1)),
																					 new Velocity(),
																					 new Force(.1f),
																					 new Damping(.9f),
																					 bounds,
																					 sphereSprite));

		// Player
		Transform playerTransform = new Transform(new Vector(0, 0), 45);
		entities.create(playerTransform,
										new Velocity(),
										new Force(10),
										new Damping(.5f),
										bounds,
										playerSprite,
//										new Spawner(1, 4, .1f, spawnSupply),
										new Input(true, actions.parseConfig(new Properties(Paths.get(ClassLoader.getSystemResource("config/keys").toURI())))));
		// Camera
		entities.create(new Transform(camera.getPosition()),
										new Chain(playerTransform.getPosition(), 1));

		primaryStage.setTitle("Pancake: Interactive Test");
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			canvas.setWidth(canvas.getWidth() + newValue.doubleValue() - oldValue.doubleValue());
			camera.setSize((float) canvas.getWidth(), (float) canvas.getHeight());
		});
		primaryStage.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
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
}
