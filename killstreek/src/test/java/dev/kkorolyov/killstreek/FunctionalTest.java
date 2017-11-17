package dev.kkorolyov.killstreek;

import dev.kkorolyov.pancake.core.component.Bounds;
import dev.kkorolyov.pancake.core.component.Chain;
import dev.kkorolyov.pancake.core.component.Input;
import dev.kkorolyov.pancake.core.component.Spawner;
import dev.kkorolyov.pancake.core.component.Sprite;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.movement.Damping;
import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.platform.Launcher;
import dev.kkorolyov.pancake.platform.action.FreeFormAction;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.math.WeightedDistribution;

import javafx.application.Application;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

import static dev.kkorolyov.killstreek.Constants.BOX;
import static dev.kkorolyov.killstreek.Constants.MAX_SPEED;
import static dev.kkorolyov.killstreek.Constants.OBJECT_DAMPING;
import static dev.kkorolyov.killstreek.Constants.OBJECT_MASS;
import static dev.kkorolyov.killstreek.Constants.PLAYER_DAMPING;
import static dev.kkorolyov.killstreek.Constants.PLAYER_MASS;
import static dev.kkorolyov.killstreek.Constants.RADIUS;
import static dev.kkorolyov.pancake.platform.event.Events.CREATED;

public class FunctionalTest extends Launcher {
	private static final Random rand = new Random();

	private final Transform playerTransform = new Transform(new Vector(), randRotation());

	public static void main(String[] args) {
		Application.launch(args);
	}

	public FunctionalTest() {
		super(new LauncherConfig()
				.title("Killstreek Functional Test")
				.size(640, 640)
				.unitPixels(new Vector(64, -64, 1)));
	}

	@Override
	public void init() throws Exception {
		initImages();
		initSounds();
		initActions();
		initEntities();
	}

	private void initImages() {
		images.put("config/images");
	}
	private void initSounds() {
		sounds.put("config/sounds");

		events.register(CREATED, (Entity e) -> {
			if (e.get(Transform.class) == playerTransform) sounds.get("spawn").play();
		});
	}
	private void initActions() {
		actions
				.put("WALK", new FreeFormAction(e -> e.get(Sprite.class).stop(false, false),
						Sprite.class))
				.put("STOP_WALK", new FreeFormAction(e -> e.get(Sprite.class).stop(true, false),
						Sprite.class))

				.put("TOGGLE_ANIMATION", new FreeFormAction(e -> {
					Sprite sprite = e.get(Sprite.class);
					sprite.stop(!sprite.isStopped(), false);
				}, Sprite.class))

				.put("TOGGLE_SPAWNER", new FreeFormAction(e -> {
					Spawner spawner = e.get(Spawner.class);
					spawner.setActive(!spawner.isActive());
				}, Sprite.class))

				.put("config/actions");
	}
	private void initEntities() {
		addGround();
		addWall();
		addBoxes(200);
		addSpheres(200);
		addPlayer();
		addCamera();
	}

	private void addGround() {
		Sprite ground = new Sprite(images.get("ground"), 3, 2, 0);
		for (int i = -15; i <= 15; i += 2) {
			for (int j = -15; j <= 15; j += 2) {
				entities.create(
						new Transform(new Vector(i, j, -1)),
						ground
				);
			}
		}
	}
	private void addWall() {
		entities.create(
				new Transform(new Vector(-3, 0), randRotation()),
				new Bounds(new Vector(3, 3)),
				new Sprite(images.get("box"))
		);
	}
	private void addBoxes(int num) {
		int line = (int) Math.sqrt(num);

		for (int i = 1; i <= line; i++) {
			for (int j = 1; j <= line; j++) {
				addObject(new Vector(i, -j), new Bounds(BOX), new Sprite(images.get("box")));
			}
		}
	}
	private void addSpheres(int num) {
		int line = (int) Math.sqrt(num);

		for (int i = 1; i <= line; i++) {
			for (int j = 1; j <= line; j++) {
				addObject(new Vector(i, j), new Bounds(RADIUS), new Sprite(images.get("sphere")));
			}
		}
	}

	private void addObject(Vector position, Bounds bounds, Sprite sprite) {
		entities.create(
				new Transform(position, randRotation()),
				new Velocity(),
				new Force(OBJECT_MASS),
				new Damping(OBJECT_DAMPING),
				new Chain(null, 0, playerTransform.getPosition()),
				bounds,
				sprite
		);
	}

	private void addPlayer() {
		Spawner spawner = new Spawner(1, 4, .1f,
				new WeightedDistribution<Supplier<Iterable<Component>>>()
						.add(1, () -> Arrays.asList(
								new Transform(new Vector(1, 1), randRotation()),
								new Velocity(),
								new Force(OBJECT_MASS),
								new Damping(OBJECT_DAMPING),
								new Bounds(BOX, RADIUS),
								new Sprite(images.get("sphere"))
						)));
		spawner.setActive(false);

		Sprite sprite = new Sprite(images.get("player"), 4, 3, 1 / 60f);
		sprite.stop(true, false);

		entities.create(
				playerTransform,
				new Velocity(MAX_SPEED),
				new Force(PLAYER_MASS),
				new Damping(PLAYER_DAMPING),
				new Bounds(BOX, RADIUS),
				spawner,
				sprite,
				new Input(true, actions.readKeys("config/keys"))
		);
	}

	private void addCamera() {
		entities.create(new Transform(camera.getPosition()),  // Chained camera
				new Chain(playerTransform.getPosition(), 1));
	}

	private static float randRotation() {
		return rand.nextInt(360);
	}
}
