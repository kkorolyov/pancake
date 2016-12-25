package dev.kkorolyov.pancake.entity;

import java.util.HashMap;
import java.util.Map;

import dev.kkorolyov.pancake.input.InputPoller;
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
	private static boolean running = true;
	private static final Label text = new Label("EMPTY");
	private static final Scene scene = new Scene(new Group(text));
	private static final InputPoller poller = new InputPoller(scene, buildKeyMap(), buildMouseKeyMap());
	private static final Entity entity = new Entity(new DynamicPoint(), new InteractiveEntityController(poller));
	
	public static void main(String[] args) {
		new Thread(() -> {
			while (running) {
				try {
					entity.update();
					Platform.runLater(() -> text.setText(entity.getPosition().toString()));
					Thread.sleep(17);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Loop done");
		}).start();
		launch(args);
		
		running = false;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(InteractiveEntityControllerTestInteractive.class.getSimpleName());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private static Map<KeyCode, Action> buildKeyMap() {
		Map<KeyCode, Action> map = new HashMap<>();
		
		map.put(KeyCode.W, e -> e.getPosition().setYVelocity(-1));
		map.put(KeyCode.A, e -> e.getPosition().setXVelocity(-1));
		map.put(KeyCode.S, e -> e.getPosition().setYVelocity(1));
		map.put(KeyCode.D, e -> e.getPosition().setXVelocity(1));
		map.put(KeyCode.ESCAPE, e -> {
			e.getPosition().setX(0);
			e.getPosition().setY(0);
		});
		return map;
	}
	private static Map<MouseButton, Action> buildMouseKeyMap() {
		Map<MouseButton, Action> map = new HashMap<>();
		
		for (MouseButton code : MouseButton.values())
			map.put(code, e -> e.getPosition().stop());
		
		return map;
	}
}
