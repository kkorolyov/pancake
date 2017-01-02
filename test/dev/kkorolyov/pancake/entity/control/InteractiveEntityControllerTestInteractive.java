package dev.kkorolyov.pancake.entity.control;

import java.util.Arrays;

import dev.kkorolyov.pancake.Renderer;
import dev.kkorolyov.pancake.entity.Body;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.Sprite;
import dev.kkorolyov.pancake.entity.collision.Bounds;
import dev.kkorolyov.pancake.entity.collision.RectangleBounds;
import dev.kkorolyov.pancake.entity.control.EntityController;
import dev.kkorolyov.pancake.entity.control.InteractiveEntityController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

@SuppressWarnings("javadoc")
public class InteractiveEntityControllerTestInteractive extends Application {
	private static final Canvas canvas = new Canvas(480, 480);
	private static final Scene scene = new Scene(new Group(canvas));
	private static final Renderer renderer = new Renderer(canvas);
	private static final Entity entity = new Entity(new RectangleBounds(0, 0, 1, 1), new Body(1), new Sprite(new Image("dev/kkorolyov/pancake/16x16.png")), buildController());
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(InteractiveEntityControllerTestInteractive.class.getSimpleName());
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				canvas.setWidth(canvas.getWidth() + newValue.doubleValue() - oldValue.doubleValue());
			}
		});
		primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				canvas.setHeight(canvas.getHeight() + newValue.doubleValue() - oldValue.doubleValue());
			}
		});
		
		new AnimationTimer() {
			private long last;
			
			public void handle(long now) {
				if (last == 0)
					last = now;
				float dt = ((float) (now - last)) / 1000000000;
				
				entity.update(dt);
				renderer.render(Arrays.asList(entity));
				drawInfo(dt);
				
				last = now;
			}
		}.start();
	}
	private void drawInfo(float dt) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		
		g.strokeText("FPS: " + Math.round(1 / dt), 0, 10);
		g.strokeText((entity.getBounds().getOrigin() + System.lineSeparator() + entity.getBody().toStringMultiline()), 0, canvas.getHeight() - 70);
	}
	
	private static EntityController buildController() {
		InteractiveEntityController controller = new InteractiveEntityController(scene);
		
		controller.addAction(KeyCode.W, e -> e.getBody().addForce(0, -10), e -> e.getBody().addForce(0, 10));
		controller.addAction(KeyCode.A, e -> e.getBody().addForce(-10, 0), e -> e.getBody().addForce(10, 0));
		controller.addAction(KeyCode.S, e -> e.getBody().addForce(0, 10), e -> e.getBody().addForce(0, -10));
		controller.addAction(KeyCode.D, e -> e.getBody().addForce(10, 0), e -> e.getBody().addForce(-10, 0));
		controller.addAction(KeyCode.SHIFT, e -> e.getBody().setMaxSpeed(10), e -> e.getBody().setMaxSpeed(5));
		controller.addAction(KeyCode.ESCAPE, e -> e.getBounds().getOrigin().set(0, 0), null);
		
		controller.addAction(MouseButton.PRIMARY, e -> e.getBody().setMaxSpeed(1), e -> e.getBody().setMaxSpeed(5));
		controller.addAction(MouseButton.SECONDARY, e -> e.getBody().setMaxSpeed(-100), e -> e.getBody().setMaxSpeed(5));
		return controller;
	}
}
