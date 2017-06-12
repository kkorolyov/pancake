package dev.kkorolyov.pancake;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import dev.kkorolyov.pancake.component.Chain;
import dev.kkorolyov.pancake.component.Input;
import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.component.collision.BoxBounds;
import dev.kkorolyov.pancake.component.collision.SphereBounds;
import dev.kkorolyov.pancake.component.movement.Damping;
import dev.kkorolyov.pancake.component.movement.Force;
import dev.kkorolyov.pancake.component.movement.MaxSpeed;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.entity.EntityPool;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.input.Action;
import dev.kkorolyov.pancake.input.ActionPool;
import dev.kkorolyov.pancake.math.Vector;
import dev.kkorolyov.pancake.system.*;
import dev.kkorolyov.pancake.system.CollisionSystem.BoxCollisionSystem;
import dev.kkorolyov.pancake.system.CollisionSystem.SphereCollisionSystem;
import dev.kkorolyov.simpleprops.Properties;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Interactive extends Application {
	private static final float MOVE_FORCE = 500;

	private Canvas canvas = new Canvas(560, 560);
	private Scene scene = new Scene(new Group(canvas));
	private ActionPool actions = buildActionPool();

	public static void main(String[] args) throws URISyntaxException {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Vector camera = new Vector(10, 10);

		Signature.index(BoxBounds.class,
										SphereBounds.class,
										Damping.class,
										Force.class,
										MaxSpeed.class,
										Sprite.class,
										Transform.class,
										Chain.class,
										Velocity.class,
										Input.class);
		GameEngine engine = new GameEngine(
				new DampingSystem(),
				new AccelerationSystem(),
				new SpeedCapSystem(),
				new MovementSystem(),
				new BoxCollisionSystem(),
				new SphereCollisionSystem(),
				new InputSystem(scene),
				new ChainSystem(),
				new RenderSystem(canvas, 32, camera));
		new GameLoop(engine).start();

		EntityPool entities = engine.getEntities();

		Sprite playerSprite = new Sprite(new Image("rotate-happy_128.png"), 32, 32, 1 / 60f);
		Sprite sphereSprite = new Sprite(new Image("scrumple_32.png"));
		Sprite boxSprite = new Sprite(new Image("sqlob_32.png"));

		BoxBounds boxBounds = new BoxBounds(new Vector(1, 1, 0));
		SphereBounds sphereBounds = new SphereBounds(.5f);

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

		// Player
		Transform playerTransform = new Transform(new Vector(0, 0));
		entities.create(playerTransform,
										new Velocity(),
										new Force(10),
										new Damping(.5f),
										boxBounds,
										sphereBounds,
										playerSprite,
										new Input(actions.parseConfig(new Properties(Paths.get(ClassLoader.getSystemResource("keys").toURI())))));
		// Camera
		entities.create(new Transform(camera),
										new Chain(playerTransform, 1));

		primaryStage.setTitle("Pancake: Interactive Test");
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			canvas.setWidth(canvas.getWidth() + newValue.doubleValue() - oldValue.doubleValue());
		});
		primaryStage.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			canvas.setHeight(canvas.getHeight() + newValue.doubleValue() - oldValue.doubleValue());
		});
	}

	private ActionPool buildActionPool() {
		ActionPool actions = new ActionPool();

		actions.put(new Action("FORCE_UP", e -> e.get(Force.class).addForce(0, MOVE_FORCE)));
		actions.put(new Action("FORCE_DOWN", e -> e.get(Force.class).addForce(0, -MOVE_FORCE)));
		actions.put(new Action("FORCE_LEFT", e -> e.get(Force.class).addForce(-MOVE_FORCE, 0)));
		actions.put(new Action("FORCE_RIGHT", e -> e.get(Force.class).addForce(MOVE_FORCE, 0)));
		actions.put(new Action("RESET", e -> e.get(Transform.class).getPosition().set(0, 0)));

		try {
			actions.put(new Properties(Paths.get(ClassLoader.getSystemResource("actions").toURI())));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return actions;
	}
}
