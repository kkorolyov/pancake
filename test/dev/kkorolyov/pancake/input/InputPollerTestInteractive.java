package dev.kkorolyov.pancake.input;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import dev.kkorolyov.pancake.entity.Action;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

@SuppressWarnings("javadoc")
public class InputPollerTestInteractive extends Application {
	private static boolean running = true;
	private static final Label text = new Label("0 actions polled");
	private static final Scene scene = new Scene(new Group(text));
	
	public static void main(String[] args) {
		InputPoller q = new InputPoller(scene, buildKeyMap(), buildMouseKeyMap());
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (running) {
					try {
						Platform.runLater(() -> text.setText(q.poll().size() + " actions polled"));
						Thread.sleep(17);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("Polling done");
			}
		}).start();
		launch(args);
		
		running = false;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("InputQueue Test");
		//primaryStage.setWidth(360);
		//primaryStage.setHeight(240);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private static Map<KeyCode, Action> buildKeyMap() {
		Map<KeyCode, Action> map = new HashMap<>();
		
		for (KeyCode code : KeyCode.values())
			map.put(code, entity -> System.out.println(code));
		
		return Collections.unmodifiableMap(map);
	}
	private static Map<MouseButton, Action> buildMouseKeyMap() {
		Map<MouseButton, Action> map = new HashMap<>();
		
		for (MouseButton code : MouseButton.values())
			map.put(code, entity -> System.out.println(code));
		
		return Collections.unmodifiableMap(map);
	}
}
