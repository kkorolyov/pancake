package dev.kkorolyov.pancake.entity.control;

import java.util.Arrays;

import dev.kkorolyov.pancake.entity.Bounds;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.Physics;
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
	private static final Entity entity = new Entity(new Bounds(2, 0), new Physics(2), buildController());
	
	public static void main(String[] args) {
		launch(args);
	}
	private static String toStringPosition(Entity entity) {
		return Arrays.toString(entity.getBounds().getPositions()) + " " + Arrays.toString(entity.getPhysics().getVelocities());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(InteractiveEntityControllerTestInteractive.class.getSimpleName());
		primaryStage.setScene(scene);
		primaryStage.setWidth(200);
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
		
		controller.addAction(KeyCode.W, e -> e.move(1, -1));
		controller.addAction(KeyCode.A, e -> e.move(0, -1));
		controller.addAction(KeyCode.S, e -> e.move(1, 1));
		controller.addAction(KeyCode.D, e -> e.move(0, 1));
		controller.addAction(KeyCode.SHIFT, e -> {
			for (int i = 0; i < e.getBounds().axes(); i++)
				e.getPhysics().setMaxVelocity(i, 10);
		});
		controller.addAction(KeyCode.ESCAPE, e -> {
			e.getBounds().setPosition(0, 0);
			e.getBounds().setPosition(1, 0);
		});
		controller.addAction(MouseButton.PRIMARY, e -> e.getPhysics().setMaxVelocity(0, 10));
		controller.addAction(MouseButton.SECONDARY, e -> e.getPhysics().setMaxVelocity(1, 10));
		return controller;
	}
}
