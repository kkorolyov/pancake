package dev.kkorolyov.pancake.garbage;

import dev.kkorolyov.pancake.component.*;
import dev.kkorolyov.pancake.component.movement.Damping;
import dev.kkorolyov.pancake.component.movement.Force;
import dev.kkorolyov.pancake.component.movement.MaxSpeed;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.math.Vector;
import dev.kkorolyov.pancake.system.MovementSystem;
import dev.kkorolyov.pancake.system.RenderSystem;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

@SuppressWarnings("javadoc")
public class EngineTestInteractive extends Application {
	final Canvas canvas = new Canvas(640, 640);
	final Scene scene = new Scene(new Group(canvas));
	final Engine engine = buildEngine();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Pancake Engine Test");
		primaryStage.setScene(scene);
		primaryStage.show();
		addCanvasResizeListeners(primaryStage);
		
		new AnimationTimer() {
			private long 	last,
										total;
			
			@Override
			public void handle(long now) {
				if (last == 0)
					last = now;
				float dt = (float) (now - last) / 1000000000;
				
				engine.update(dt);
				
				total += (now - last);
				last = now;
				
				GraphicsContext g = canvas.getGraphicsContext2D();
				g.strokeText("FPS: " + Math.round(1 / dt), 0, 10);
			}
		}.start();
	}
	private void addCanvasResizeListeners(Stage stage) {
		stage.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				canvas.setWidth(canvas.getWidth() + newValue.doubleValue() - oldValue.doubleValue());
			}
		});
		stage.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				canvas.setHeight(canvas.getHeight() + newValue.doubleValue() - oldValue.doubleValue());
			}
		});
	}
	
	private Engine buildEngine() {
		Engine engine = new Engine(new MovementSystem(), new RenderSystem(canvas));

		Velocity velocity = new Velocity();
		MaxSpeed maxSpeed = new MaxSpeed(10);
		Force force = new Force(1);
		force.addForce(5, 5);
		Damping damping = new Damping(.9f);
		Transform transform = new Transform(new Vector());
		Sprite sprite = new Sprite(new Image("16x16.png"));
		
		engine.getEntities().create(velocity, maxSpeed, force, damping, transform, sprite);
		
		return engine;
	}
}
