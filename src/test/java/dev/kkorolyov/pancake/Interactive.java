package dev.kkorolyov.pancake;

import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.component.collision.RectangleBounds;
import dev.kkorolyov.pancake.component.movement.Force;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.core.EntityPool;
import dev.kkorolyov.pancake.core.GameEngine;
import dev.kkorolyov.pancake.core.GameLoop;
import dev.kkorolyov.pancake.math.Vector;
import dev.kkorolyov.pancake.system.CollisionSystem;
import dev.kkorolyov.pancake.system.MovementSystem;
import dev.kkorolyov.pancake.system.RenderSystem;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Interactive extends Application {
	private Canvas canvas = new Canvas(560, 560);
	private Scene scene = new Scene(new Group(canvas));

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		GameEngine engine = new GameEngine(
				new MovementSystem(),
				new CollisionSystem(),
				new RenderSystem(canvas)
		);
		new GameLoop(engine).start();

		EntityPool entities = engine.getEntities();
		entities.create(new Transform(new Vector(10, 10)),
										new Velocity(),
										new Force(1),
										new RectangleBounds(16, 16),
										new Sprite(new Image("16x16.png")));

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
