package dev.kkorolyov.pancake.entity.control;

import java.util.Arrays;

import dev.kkorolyov.pancake.Renderer;
import dev.kkorolyov.pancake.entity.Body;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.Sprite;
import dev.kkorolyov.pancake.entity.collision.RectangleBounds;
import dev.kkorolyov.pancake.input.KeyAction;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

@SuppressWarnings("javadoc")
public class InteractiveEntityControllerTestInteractive extends Application {
	private static final Canvas canvas = new Canvas(480, 480);
	private static final Scene scene = new Scene(new Group(canvas));
	private static final Renderer renderer = new Renderer(canvas);
	private static final Entity entity = new Entity(new RectangleBounds(0, 0, 1, 1), new Body(1), new Sprite(new Image("16x16.png")), buildController());
	
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
		
		controller.addAction(new KeyAction(e -> e.getBody().addForce(0, -10), null, e -> e.getBody().addForce(0, 10), KeyCode.W));
		controller.addAction(new KeyAction(e -> e.getBody().addForce(-10, 0), null, e -> e.getBody().addForce(10, 0), KeyCode.A));
		controller.addAction(new KeyAction(e -> e.getBody().addForce(0, 10), null, e -> e.getBody().addForce(0, -10), KeyCode.S));
		controller.addAction(new KeyAction(e -> e.getBody().addForce(10, 0), null, e -> e.getBody().addForce(-10, 0), KeyCode.D));
		controller.addAction(new KeyAction(e -> e.getBody().setMaxSpeed(10), null, e -> e.getBody().setMaxSpeed(5), KeyCode.SHIFT));
		controller.addAction(new KeyAction(e -> e.getBounds().getOrigin().set(0, 0), null, null, KeyCode.ESCAPE));
		
		controller.addAction(new KeyAction(e -> e.getBody().setMaxSpeed(1), null, e -> e.getBody().setMaxSpeed(5), MouseButton.PRIMARY));
		controller.addAction(new KeyAction(e -> e.getBody().setMaxSpeed(-100), null, e -> e.getBody().setMaxSpeed(5), MouseButton.SECONDARY));
		return controller;
	}
}
