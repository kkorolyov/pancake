package dev.kkorolyov.thing.gui;

import java.util.LinkedList;
import java.util.Queue;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

@SuppressWarnings("javadoc")
public class TestFX extends Application {
	private final Queue<KeyEvent> q = new LinkedList<>();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Test Title");
		
		Canvas canvas = new Canvas(480, 360);
		
		Scene scene = new Scene(new Group(canvas));
		scene.setOnKeyPressed(key -> {
			q.add(key);
			System.out.println("Added " + key.getText());
		});
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Box box = new Box();
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				System.out.println(now);
				
				if (!q.isEmpty()) {
					switch (q.remove().getCode()) {
						case W:
							if (box.y > 0)
								box.y-=10;
							break;
						case S:
							if (box.y < canvas.getHeight())
								box.y+=10;
							break;
						case A:
							if (box.x > 0)
								box.x-=10;
							break;
						case D:
							if (box.x < canvas.getWidth())
								box.x+=10;
						default:
							break;
					}
				}
				
				GraphicsContext g = canvas.getGraphicsContext2D();
				g.setFill(Color.BLACK);
				g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
				
				g.setFont(Font.font(32));
				g.setStroke(Color.DARKCYAN);
				g.strokeText("Yo", box.x, box.y);
			}
		};
		timer.start();
	}
	
	private class Box {
		int x = 0,
				y = 0,
				xV = 0,
				yY = 0;
	}
}
