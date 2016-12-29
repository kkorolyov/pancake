package dev.kkorolyov.pancake.entity.control;

import java.util.Arrays;

import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.control.EntityController;
import dev.kkorolyov.pancake.entity.control.InteractiveEntityController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

@SuppressWarnings("javadoc")
public class InteractiveEntityControllerTestInteractive extends Application {
	private static final Label text = new Label("EMPTY");
	private static final Scene scene = new Scene(new Group(text));
	private static final Entity entity = new Entity(2, buildController());
	
	public static void main(String[] args) {
		launch(args);
	}
	private static String toStringPosition(Entity entity) {
		int[] position = new int[entity.getAxes()],
					velocity = new int[position.length];
		for (int i = 0; i < position.length; i++) {
			position[i] = entity.getPosition(i);
			velocity[i] = entity.getVelocity(i);
		}
		return Arrays.toString(position) + " " + Arrays.toString(velocity);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(InteractiveEntityControllerTestInteractive.class.getSimpleName());
		primaryStage.setScene(scene);
		primaryStage.show();
		
		new AnimationTimer() {
			public void handle(long now) {
				entity.update();
				Platform.runLater(() -> text.setText(toStringPosition(entity)));
			}
		}.start();
	}
	
	private static EntityController buildController() {
		InteractiveEntityController controller = new InteractiveEntityController(scene);
		
		controller.addAction(KeyCode.W, e -> e.setVelocity(1, -1));
		controller.addAction(KeyCode.A, e -> e.setVelocity(0, -1));
		controller.addAction(KeyCode.S, e -> e.setVelocity(1, 1));
		controller.addAction(KeyCode.D, e -> e.setVelocity(0, 1));
		controller.addAction(KeyCode.ESCAPE, e -> {
			e.setPosition(0, 0);
			e.setPosition(1, 0);
		});
		for (MouseButton code : MouseButton.values())
			controller.addAction(code, e -> e.stop());
		
		return controller;
	}
}
