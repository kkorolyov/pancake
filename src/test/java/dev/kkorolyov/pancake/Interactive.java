package dev.kkorolyov.pancake;

import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.component.collision.RectangleBounds;
import dev.kkorolyov.pancake.component.control.EntityController;
import dev.kkorolyov.pancake.component.control.InteractiveEntityController;
import dev.kkorolyov.pancake.component.movement.Damping;
import dev.kkorolyov.pancake.component.movement.Force;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.core.EntityPool;
import dev.kkorolyov.pancake.core.GameEngine;
import dev.kkorolyov.pancake.core.GameLoop;
import dev.kkorolyov.pancake.input.KeyAction;
import dev.kkorolyov.pancake.math.Vector;
import dev.kkorolyov.pancake.system.*;
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
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		GameEngine engine = new GameEngine(
				new DampingSystem(),
				new AccelerationSystem(),
				new SpeedCapSystem(),
				new MovementSystem(),
				new CollisionSystem(),
				new ActionSystem(),
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
										buildController());

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

	private EntityController buildController() {
		return new InteractiveEntityController(scene,
																					 new KeyAction(e -> e.get(Force.class).addForce(0, -MOVE_FORCE), null, e -> e.get(Force.class).addForce(0, MOVE_FORCE), KeyCode.W),
																					 new KeyAction(e -> e.get(Force.class).addForce(-MOVE_FORCE, 0), null, e -> e.get(Force.class).addForce(MOVE_FORCE, 0), KeyCode.A),
																					 new KeyAction(e -> e.get(Force.class).addForce(0, MOVE_FORCE), null, e -> e.get(Force.class).addForce(0, -MOVE_FORCE), KeyCode.S),
																					 new KeyAction(e -> e.get(Force.class).addForce(MOVE_FORCE, 0), null, e -> e.get(Force.class).addForce(-MOVE_FORCE, 0), KeyCode.D),
																					 new KeyAction(e -> e.get(Transform.class).getPosition().set(0, 0), null, null, KeyCode.ESCAPE)
																					 );
	}
}
