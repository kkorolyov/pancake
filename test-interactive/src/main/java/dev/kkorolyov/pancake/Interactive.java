package dev.kkorolyov.pancake;

import dev.kkorolyov.pancake.component.Bounds;
import dev.kkorolyov.pancake.component.Chain;
import dev.kkorolyov.pancake.component.Input;
import dev.kkorolyov.pancake.component.Spawner;
import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.component.movement.Damping;
import dev.kkorolyov.pancake.component.movement.Force;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.entity.Component;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.EntityPool;
import dev.kkorolyov.pancake.event.PlatformEvents;
import dev.kkorolyov.pancake.input.Action;
import dev.kkorolyov.pancake.math.Vector;
import dev.kkorolyov.pancake.math.WeightedDistribution;
import dev.kkorolyov.simpleprops.Properties;

import javafx.application.Application;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

public class Interactive extends Launcher {
	private static final float MOVE_FORCE = 500;
	private static final float MAX_SPEED = 50;
	private static final float OBJECT_MASS = .1f;
	private static final float PLAYER_MASS = 10;
	private static final float OBJECT_DAMPING = .9f;
	private static final float PLAYER_DAMPING = .5f;
	private static final Vector BOX = new Vector(1, 1);
	private static final float RADIUS = BOX.getX() / 2;

	private static final Random rand = new Random();

	private final Transform playerTransform = new Transform(new Vector(), randRotation());

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	protected LauncherConfig config() {
		return new LauncherConfig()
				.title("Pancake: Interactive Test")
				.size(560, 560)
				.unitPixels(new Vector(64, -64, 1));
	}
	@Override
	protected Iterable<Class<? extends Component>> components() {
		return Arrays.asList(Bounds.class,
				Damping.class,
				Force.class,
				Sprite.class,
				Transform.class,
				Chain.class,
				Velocity.class,
				Input.class,
				Spawner.class);
	}

	@Override
	public void init() throws Exception {
		initImages();
		initSounds();
		initActions();
		initEntities(engine.getEntities());
	}
	private void initImages() throws Exception {
		images.put(new Properties(Paths.get(ClassLoader.getSystemResource("config/images").toURI())));
	}
	private void initSounds() throws Exception {
		sounds.put(new Properties(Paths.get(ClassLoader.getSystemResource("config/sounds").toURI())));

		engine.getEvents().register(PlatformEvents.CREATED, (Entity e) -> {
			if (e.get(Input.class) != null) sounds.get("spawn").play();
		});
	}
	private void initActions() throws Exception {
		actions.put(new Action("FORCE_UP", e -> e.get(Force.class).getForce().translate(0, MOVE_FORCE)));
		actions.put(new Action("FORCE_DOWN", e -> e.get(Force.class).getForce().translate(0, -MOVE_FORCE)));
		actions.put(new Action("FORCE_RIGHT", e -> e.get(Force.class).getForce().translate(MOVE_FORCE, 0)));
		actions.put(new Action("FORCE_LEFT", e -> e.get(Force.class).getForce().translate(-MOVE_FORCE, 0)));
		actions.put(new Action("RESET", e -> e.get(Transform.class).getPosition().set(0, 0)));
		actions.put(new Action("WALK", e -> e.get(Sprite.class).stop(false, false)));
		actions.put(new Action("STOP", e -> e.get(Sprite.class).stop(true, false)));

		actions.put(new Properties(Paths.get(ClassLoader.getSystemResource("config/actions").toURI())));
	}
	private void initEntities(EntityPool entities) throws Exception {
		// Wall
		entities.create(new Transform(new Vector(-3, 0), randRotation()),
				new Bounds(new Vector(5, 21)),
				new Sprite(images.get("box")));

		addGround(entities);
		addBoxes(entities, 200);
		addSpheres(entities, 200);
		addPlayer(entities);
	}

	private void addGround(EntityPool entities) {
		Sprite ground = new Sprite(images.get("ground"), 3, 2, 0);
		for (int i = -15; i <= 15; i += 2) {
			for (int j = -15; j <= 15; j += 2) {
				entities.create(new Transform(new Vector(i, j, -1)),
						ground);
			}
		}
	}

	private void addPlayer(EntityPool entities) {
		try {
			Sprite sprite = new Sprite(images.get("player"), 4, 3, 1 / 60f);
			sprite.stop(true, false);

			entities.create(playerTransform,
					new Velocity(MAX_SPEED),
					new Force(PLAYER_MASS),
					new Damping(PLAYER_DAMPING),
					new Bounds(BOX, RADIUS),
					sprite,
					// buildSpawner(),
					new Input(true, actions.parseConfig(new Properties(Paths.get(ClassLoader.getSystemResource("config/keys").toURI())))));

			entities.create(new Transform(camera.getPosition()),  // Chained camera
					new Chain(playerTransform.getPosition(), 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addSpheres(EntityPool entities, int num) {
		int line = (int) Math.sqrt(num);

		for (int i = 1; i <= line; i++) {
			for (int j = 1; j <= line; j++) {
				addObject(entities, new Vector(i, j), new Bounds(RADIUS), new Sprite(images.get("sphere")));
			}
		}
	}
	private void addBoxes(EntityPool entities, int num) {
		int line = (int) Math.sqrt(num);

		for (int i = 1; i <= line; i++) {
			for (int j = 1; j <= line; j++) {
				addObject(entities, new Vector(i, -j), new Bounds(BOX), new Sprite(images.get("box")));
			}
		}
	}

	private void addObject(EntityPool entities, Vector position, Bounds bounds, Sprite sprite) {
		entities.create(new Transform(position, randRotation()),
				new Velocity(),
				new Force(OBJECT_MASS),
				new Damping(OBJECT_DAMPING),
				new Chain(null, 0, playerTransform.getPosition()),
				bounds,
				sprite);
	}

	private Spawner buildSpawner() {
		return new Spawner(1, 4, .1f, new WeightedDistribution<Supplier<Iterable<Component>>>()
				.add(1, () -> Arrays.asList(new Transform(new Vector(1, 1), randRotation()),
						new Velocity(),
						new Force(OBJECT_MASS),
						new Damping(OBJECT_DAMPING),
						new Bounds(BOX, RADIUS),
						new Sprite(images.get("sphere")))));
	}

	private float randRotation() {
		return rand.nextInt(360);
	}
}
