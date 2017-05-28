package dev.kkorolyov.pancake.control;

import java.util.*;

import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.engine.Entity;
import dev.kkorolyov.pancake.collision.RectangleBounds;
import dev.kkorolyov.pancake.input.KeyAction;
import dev.kkorolyov.pancake.system.RenderSystem;
import garbage.Body;
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
	private static final float MOVE_FORCE = 100;

	private static final Canvas canvas = new Canvas(560, 560);
	private static final Scene scene = new Scene(new Group(canvas));
	private static final RenderSystem renderer = new RenderSystem(canvas);
	private static final Entity entity = new Entity(new RectangleBounds(0, 0, 16, 16), new Body(1), new Sprite(new Image("16x16.png")), buildController()),
			box = new Entity(new RectangleBounds(100, 100, 32, 32), new Body(.1f), new Sprite(new Image("32x32.png")), null);

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
		Set<Entity> walls = buildWalls();
		Entity[] bouncies = buildBouncies(1000);

		new AnimationTimer() {
			private long last,
					total;

			public void handle(long now) {
				if (last == 0) last = now;
				float dt = ((float) (now - last)) / 1000000000;
				//entity.update(dt);
				//box.update(dt);

				//entity.collide(box);

				for (Entity bouncy : bouncies) {
					bouncy.update(dt);

					if (total > 1000000000)
						bouncy.getBody().setForce(0, 0);

				}
				for (int i = 0; i < bouncies.length; i++) {
					for (int j = i + 1; j < bouncies.length; j++)
						bouncies[i].collide(bouncies[j]);
				}
				for (Entity wall : walls) {
					//entity.collide(wall);
					//box.collide(wall);
					for (Entity bouncy : bouncies)
						bouncy.collide(wall);
				}
				//renderer.render(Arrays.asList(entity, box));
				renderer.render(Arrays.asList(bouncies));
				drawInfo(dt);

				total += (now - last);
				last = now;
			}
		}.start();
	}
	private void drawInfo(float dt) {
		GraphicsContext g = canvas.getGraphicsContext2D();

		g.strokeText("FPS: " + Math.round(1 / dt), 0, 10);
		g.strokeText(("Player: " + entity.getBounds() + System.lineSeparator() + "\t" + entity.getBody()), 0, canvas.getHeight() - 50);
		g.strokeText(("Box: " + box.getBounds() + System.lineSeparator() + "\t" + box.getBody()), 0, canvas.getHeight() - 20);
	}

	private static EntityController buildController() {
		InteractiveEntityController controller = new InteractiveEntityController(scene);
		controller.setActions(new KeyAction(e -> e.getBody().addForce(0, -MOVE_FORCE), null, e -> e.getBody().addForce(0, MOVE_FORCE), KeyCode.W),
													new KeyAction(e -> e.getBody().addForce(-MOVE_FORCE, 0), null, e -> e.getBody().addForce(MOVE_FORCE, 0), KeyCode.A),
													new KeyAction(e -> e.getBody().addForce(0, MOVE_FORCE), null, e -> e.getBody().addForce(0, -MOVE_FORCE), KeyCode.S),
													new KeyAction(e -> e.getBody().addForce(MOVE_FORCE, 0), null, e -> e.getBody().addForce(-MOVE_FORCE, 0), KeyCode.D),
													new KeyAction(e -> e.getBody().setMaxSpeed(10), null, e -> e.getBody().setMaxSpeed(5), KeyCode.SHIFT),
													new KeyAction(e -> {
														e.getBounds().getOrigin().set(0, 0);
														box.getBounds().getOrigin().set(100, 100);
													}, null, null, KeyCode.ESCAPE));

		controller.setActions(new KeyAction(e -> e.getBody().setMaxSpeed(1), null, e -> e.getBody().setMaxSpeed(5), MouseButton.PRIMARY),
													new KeyAction(e -> e.getBody().setMaxSpeed(-100), null, e -> e.getBody().setMaxSpeed(5), MouseButton.SECONDARY));
		return controller;
	}

	private static Set<Entity> buildWalls() {
		Set<Entity> walls = new HashSet();

		walls.add(new Entity(new RectangleBounds(-100, 0, 100, (float) canvas.getHeight()), new Body(1000000), null, null));
		walls.add(new Entity(new RectangleBounds(0, -100, (float) canvas.getWidth(), 100), new Body(1000000), null, null));
		walls.add(new Entity(new RectangleBounds((float) canvas.getWidth() + .1f, 0, 100, (float) canvas.getHeight()), new Body(1000000), null, null));
		walls.add(new Entity(new RectangleBounds(0, (float) canvas.getHeight() + .1f, (float) canvas.getWidth(), 100), new Body(1000000), null, null));

		return walls;
	}
	private static Entity[] buildBouncies(int num) {
		List<Entity> bouncies = new LinkedList<>();
		float size = 16,
				mass = 1;
		int maxForce = 10;
		Image image = new Image("16x16.png");

		Random rand = new Random();
		for (int i = 0; i < num; i++) {
			Entity bouncy = new Entity(new RectangleBounds(rand.nextInt((int) (canvas.getWidth() - size)), rand.nextInt((int) (canvas.getHeight() - size)), size, size), new Body(mass), new Sprite(image), null);
			bouncy.getBody().setDamping(1);
			bouncy.getBody().addForce(rand.nextInt(maxForce) * (rand.nextBoolean() ? 1 : -1), rand.nextInt(maxForce) * (rand.nextBoolean() ? 1 : -1));
			bouncies.add(bouncy);
		}
		return bouncies.toArray(new Entity[bouncies.size()]);
	}
}
