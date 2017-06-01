package dev.kkorolyov.pancake;

import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.component.collision.EllipseBounds;
import dev.kkorolyov.pancake.component.collision.RectangleBounds;
import dev.kkorolyov.pancake.component.Input;
import dev.kkorolyov.pancake.component.movement.Damping;
import dev.kkorolyov.pancake.component.movement.Force;
import dev.kkorolyov.pancake.component.movement.MaxSpeed;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.input.KeyAction;
import dev.kkorolyov.pancake.math.Vector;
import dev.kkorolyov.pancake.system.*;
import dev.kkorolyov.simplelogs.Level;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simplelogs.append.Appenders;
import dev.kkorolyov.simplelogs.format.Formatters;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Interactive extends Application {
	private static final float MOVE_FORCE = 1000;

	private Canvas canvas = new Canvas(560, 560);
	private Scene scene = new Scene(new Group(canvas));

	public static void main(String[] args) {
		Logger.getLogger("dev.kkorolyov.pancake", Level.DEBUG, Formatters.simple(), Appenders.err(Level.DEBUG));

		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Signature.index(RectangleBounds.class,
										EllipseBounds.class,
										Damping.class,
										Force.class,
										MaxSpeed.class,
										Sprite.class,
										Transform.class,
										Velocity.class,
										Input.class);
		GameEngine engine = new GameEngine(
				new DampingSystem(),
				new AccelerationSystem(),
				new SpeedCapSystem(),
				new MovementSystem(),
				new CollisionSystem(),
				new InputSystem(scene),
				new RenderSystem(canvas)
		);
		new GameLoop(engine).start();

		EntityPool entities = engine.getEntities();
		entities.create(new Transform(new Vector(10, 10)),
										new Velocity(),
										new Force(1),
										new Damping(.5f),
										new RectangleBounds(32, 32),
										new Sprite(new Image("32x32.png")),
										new Input(new KeyAction(e -> e.get(Force.class).addForce(0, -MOVE_FORCE), null, e -> e.get(Force.class).addForce(0, MOVE_FORCE), KeyCode.W),
															new KeyAction(e -> e.get(Force.class).addForce(-MOVE_FORCE, 0), null, e -> e.get(Force.class).addForce(MOVE_FORCE, 0), KeyCode.A),
															new KeyAction(e -> e.get(Force.class).addForce(0, MOVE_FORCE), null, e -> e.get(Force.class).addForce(0, -MOVE_FORCE), KeyCode.S),
															new KeyAction(e -> e.get(Force.class).addForce(MOVE_FORCE, 0), null, e -> e.get(Force.class).addForce(-MOVE_FORCE, 0), KeyCode.D),
															new KeyAction(e -> e.get(Transform.class).getPosition().set(0, 0), null, null, KeyCode.ESCAPE)));

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
}
