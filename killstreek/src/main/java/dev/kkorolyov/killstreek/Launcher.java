package dev.kkorolyov.killstreek;

import dev.kkorolyov.killstreek.component.Damage;
import dev.kkorolyov.killstreek.component.Health;
import dev.kkorolyov.killstreek.media.HealthBar;
import dev.kkorolyov.killstreek.media.Sprite;
import dev.kkorolyov.pancake.core.component.ActionQueue;
import dev.kkorolyov.pancake.core.component.AudioEmitter;
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
import dev.kkorolyov.pancake.core.event.EntitiesCollided;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.event.EntityCreated;
import dev.kkorolyov.pancake.platform.event.EntityDestroyed;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.simplestructs.WeightedDistribution;

import javafx.application.Application;
import javafx.scene.media.MediaPlayer;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

public class Launcher extends dev.kkorolyov.pancake.platform.Launcher {
	private static final Random RAND = new Random();
	private static final Vector HEALTH_BAR_SIZE = new Vector(1, .25f);
	private static final Vector SPRITE_ORIENTATION_OFFSET = new Vector(0, 0, (float) (-.5 * Math.PI));

	private final Graphic groundGraphic = new Graphic(null);
	private final Graphic boxGraphic = new Graphic(null);
	private final Graphic sphereGraphic = new Graphic(null);

	private final Transform playerTransform = new Transform(new Vector(), randRotation());
	private int player;

	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
		Application.launch(args);
	}

	public Launcher() {
		super(new LauncherConfig()
				.title("Killstreek Functional Test")
				.size(640, 640)
				.unitPixels(new Vector(64, -64, 1)));

		HEALTH_BAR_SIZE.scale(camera.getUnitPixels().getX());
	}

	@Override
	public void init() {
		initImages();
		initSounds();
		initActions();
		initEntities();
		initEvents();
	}

	private void initImages() {
		images.put("config/images");

		groundGraphic.setDelegate(new Sprite(images.get("ground"), new Vector(), 3, 2, 0));
		boxGraphic.setDelegate(new Sprite(images.get("box"), SPRITE_ORIENTATION_OFFSET));
		sphereGraphic.setDelegate(new Sprite(images.get("sphere"), SPRITE_ORIENTATION_OFFSET));
	}
	private void initSounds() {
		audio.put("config/audio");
	}
	private void initActions() {
		actions
				.put("WALK", entity -> entity.get(Animation.class).setActive(true))
				.put("STOP_WALK", entity -> entity.get(Animation.class).setActive(false))

				.put("TOGGLE_ANIMATION", entity -> entity.get(Animation.class).toggle())

				.put("TOGGLE_SPAWNER", entity -> entity.get(Spawner.class).toggle())

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
		events.register(EntitiesCollided.class, e -> {
			if (player == e.getCollided()[0]) {
				EntityPool.ManagedEntity other = entities.get(e.getCollided()[1]);
				// May be destroyed
				if (other != null) {
					if (other.get(Damage.class) != null) {
						other.get(Damage.class).reset();
					} else {
						other.add(new Damage(1));
					}
				}
			}
		});

		events.register(EntityCreated.class, e -> {
			if (player == e.getId()) new MediaPlayer(audio.get("spawn")).play();
		});
		events.register(EntityDestroyed.class, e -> {
			new MediaPlayer(audio.get("spawn")).play();
		});
	}

	private void addGround() {
		for (int i = -15; i <= 15; i += 2) {
			for (int j = -15; j <= 15; j += 2) {
				entities.create()
						.add(
								new Transform(new Vector(i, j, -1)),
								groundGraphic
						);
			}
		}
	}
	private void addWall() {
		entities.create()
				.add(
						new Transform(new Vector(-3, 0), randRotation()),
						new Bounds(new Vector(3, 3)),
						boxGraphic,
						new AudioEmitter().enqueue(audio.get("wall"))
				);
	}
	private void addBoxes(int num) {
		int line = (int) Math.sqrt(num);

		for (int i = 1; i <= line; i++) {
			for (int j = 1; j <= line; j++) {
				addObject(new Vector(i, -j), new Bounds(Constants.BOX), boxGraphic);
			}
		}
	}
	private void addSpheres(int num) {
		int line = (int) Math.sqrt(num);

		for (int i = 1; i <= line; i++) {
			for (int j = 1; j <= line; j++) {
				addObject(new Vector(i, j), new Bounds(Constants.RADIUS), sphereGraphic);
			}
		}
	}

	private void addObject(Vector position, Bounds bounds, Graphic graphic) {
		Transform transform = new Transform(position, randRotation());
		Health health = new Health(20);

		// Object
		entities.create()
				.add(
						transform,
						new Velocity(),
						new Force(Constants.OBJECT_MASS),
						new Damping(Constants.OBJECT_DAMPING),
						new Chain(null, 0, playerTransform.getPosition()),
						bounds,
						graphic,
						health
				);
		// Its health bar
		entities.create()
				.add(
						new Transform(new Vector(0, .3f, 1), transform, false),
						new Graphic(new HealthBar(health, HEALTH_BAR_SIZE)),
						health,
						new Damage()  // To be visible to DamageSystem
				);
	}

	private void addPlayer() {
		Sprite sprite = new Sprite(images.get("player"), SPRITE_ORIENTATION_OFFSET, 4, 3, (long) (1e9 / 60));

		Transform transform = playerTransform;
		Spawner spawner = new Spawner(1, 4, .1f,
				new WeightedDistribution<Supplier<Iterable<Component>>>()
						.add(
								() -> Arrays.asList(
										new Transform(new Vector(1, 1), randRotation()),
										new Velocity(),
										new Force(Constants.OBJECT_MASS),
										new Damping(Constants.OBJECT_DAMPING),
										new Bounds(Constants.BOX, Constants.RADIUS),
										sphereGraphic
								),
								1
						)
		);
		spawner.setActive(false);
		Health health = new Health(100);

		player = entities.create()
				.add(
						transform,
						new Velocity(Constants.MAX_SPEED),
						new Force(Constants.PLAYER_MASS),
						new Damping(Constants.PLAYER_DAMPING),
						new Bounds(Constants.BOX, Constants.RADIUS),
						spawner,
						new Animation(sprite),
						new Graphic(sprite),
						new Input(true, actions.readKeys("config/keys")),
						health,
						new ActionQueue()
				)
				.getId();
		entities.create()
				.add(
						new Transform(new Vector(0, .3f, 1), transform, false),
						new Graphic(new HealthBar(health, HEALTH_BAR_SIZE))
				);
	}

	private void addCamera() {
		entities.create()
				.add(
						new Transform(camera.getPosition()),  // Chained camera
						new Chain(playerTransform.getPosition(), 1)
				);
	}

	private static Vector randRotation() {
		return new Vector(RAND.nextFloat(), RAND.nextFloat(), RAND.nextFloat());
	}
}
