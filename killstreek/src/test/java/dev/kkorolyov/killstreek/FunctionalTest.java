package dev.kkorolyov.killstreek;

import dev.kkorolyov.killstreek.component.Damage;
import dev.kkorolyov.killstreek.component.Health;
import dev.kkorolyov.killstreek.media.HealthBar;
import dev.kkorolyov.killstreek.media.Sprite;
import dev.kkorolyov.pancake.core.component.Bounds;
import dev.kkorolyov.pancake.core.component.Chain;
import dev.kkorolyov.pancake.core.component.Input;
import dev.kkorolyov.pancake.core.component.Spawner;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.media.Animation;
import dev.kkorolyov.pancake.core.component.media.Graphic;
import dev.kkorolyov.pancake.core.component.movement.Damping;
import dev.kkorolyov.pancake.core.component.movement.Force;
import dev.kkorolyov.pancake.core.component.movement.Velocity;
import dev.kkorolyov.pancake.core.event.Events;
import dev.kkorolyov.pancake.platform.Launcher;
import dev.kkorolyov.pancake.platform.action.FreeFormAction;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
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
import static dev.kkorolyov.pancake.platform.event.Events.DESTROYED;

public class FunctionalTest extends Launcher {
	private static final Random RAND = new Random();
	private static final Vector HEALTH_BAR_SIZE = new Vector(1, .25f);

	private final Transform playerTransform = new Transform(new Vector(), randRotation());
	private int player;

	public static void main(String[] args) {
		Application.launch(args);
	}

	public FunctionalTest() {
		super(new LauncherConfig()
				.title("Killstreek Functional Test")
				.size(640, 640)
				.unitPixels(new Vector(64, -64, 1)));

		HEALTH_BAR_SIZE.scale(camera.getUnitPixels().getX());
	}

	@Override
	public void init() throws Exception {
		initImages();
		initSounds();
		initActions();
		initEntities();
		initEvents();
	}

	private void initImages() {
		images.put("config/images");
	}
	private void initSounds() {
		sounds.put("config/sounds");
	}
	private void initActions() {
		actions
				.put("WALK", new FreeFormAction(e -> e.get(Animation.class).setActive(true),
						Animation.class))
				.put("STOP_WALK", new FreeFormAction(e -> e.get(Animation.class).setActive(false),
						Animation.class))

				.put("TOGGLE_ANIMATION", new FreeFormAction(e -> {
					Animation animation = e.get(Animation.class);
					animation.setActive(!animation.isActive());
				}, Animation.class))

				.put("TOGGLE_SPAWNER", new FreeFormAction(e -> {
					Spawner spawner = e.get(Spawner.class);
					spawner.setActive(!spawner.isActive());
				}, Spawner.class))

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
	private void initEvents() {
		Signature damageSig = new Signature(Damage.class);

		events.register(Events.COLLIDED, (Entity[] colliders) -> {
			if (player == colliders[0].getId()) {
				if (colliders[1].contains(damageSig)) colliders[1].get(Damage.class).reset();
				else colliders[1].add(new Damage(1));
			}
		});

		events.register(CREATED, (Entity e) -> {
			if (player == e.getId()) sounds.get("spawn").play();
		});
		events.register(DESTROYED, (Entity e) -> {
			sounds.get("spawn").play();
		});
	}

	private void addGround() {
		Graphic ground = new Graphic(new Sprite(images.get("ground"), 3, 2, 0));
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
				new Graphic(new Sprite(images.get("box")))
		);
	}
	private void addBoxes(int num) {
		Graphic graphic = new Graphic(new Sprite(images.get("box")));
		int line = (int) Math.sqrt(num);

		for (int i = 1; i <= line; i++) {
			for (int j = 1; j <= line; j++) {
				addObject(new Vector(i, -j), new Bounds(BOX), graphic);
			}
		}
	}
	private void addSpheres(int num) {
		Graphic graphic = new Graphic(new Sprite(images.get("sphere")));
		int line = (int) Math.sqrt(num);

		for (int i = 1; i <= line; i++) {
			for (int j = 1; j <= line; j++) {
				addObject(new Vector(i, j), new Bounds(RADIUS), graphic);
			}
		}
	}

	private void addObject(Vector position, Bounds bounds, Graphic graphic) {
		Health health = new Health(20);

		// Object
		entities.create(
				new Transform(position, randRotation()),
				new Velocity(),
				new Force(OBJECT_MASS),
				new Damping(OBJECT_DAMPING),
				new Chain(null, 0, playerTransform.getPosition()),
				bounds,
				graphic,
				health
		);
		// Its health bar
		entities.create(
				new Transform(new Vector()),
				new Chain(position, 0),
				new Graphic(new HealthBar(health, HEALTH_BAR_SIZE)),
				health,
				new Damage()	// To be visible to DamageSystem
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
								new Graphic(new Sprite(images.get("sphere")))
						)));
		spawner.setActive(false);

		Sprite sprite = new Sprite(images.get("player"), 4, 3, 1 / 60f);

		player = entities.create(
				playerTransform,
				new Velocity(MAX_SPEED),
				new Force(PLAYER_MASS),
				new Damping(PLAYER_DAMPING),
				new Bounds(BOX, RADIUS),
				spawner,
				new Animation(sprite),
				new Graphic(sprite),
				new Input(true, actions.readKeys("config/keys"))
		).getId();
	}

	private void addCamera() {
		entities.create(new Transform(camera.getPosition()),  // Chained camera
				new Chain(playerTransform.getPosition(), 1));
	}

	private static float randRotation() {
		return RAND.nextInt(360);
	}
}
