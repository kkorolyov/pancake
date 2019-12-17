package dev.kkorolyov.killstreek

import dev.kkorolyov.killstreek.component.Damage
import dev.kkorolyov.killstreek.component.Health
import dev.kkorolyov.killstreek.media.HealthBar
import dev.kkorolyov.killstreek.media.Sprite
import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.core.component.AudioEmitter
import dev.kkorolyov.pancake.core.component.Bounds
import dev.kkorolyov.pancake.core.component.Chain
import dev.kkorolyov.pancake.core.component.Input
import dev.kkorolyov.pancake.core.component.Spawner
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.core.component.media.Animation
import dev.kkorolyov.pancake.core.component.media.Graphic
import dev.kkorolyov.pancake.core.component.movement.Damping
import dev.kkorolyov.pancake.core.component.movement.Force
import dev.kkorolyov.pancake.core.component.movement.Velocity
import dev.kkorolyov.pancake.core.event.EntitiesCollided
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.GameLoop
import dev.kkorolyov.pancake.platform.Resources
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.application.Application.Config
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.event.EntityCreated
import dev.kkorolyov.pancake.platform.event.EntityDestroyed
import dev.kkorolyov.pancake.platform.event.EventBroadcaster.Managed
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.media.audio.Audio
import dev.kkorolyov.pancake.platform.media.audio.Audio.State.PLAY
import dev.kkorolyov.pancake.platform.media.graphic.CompositeRenderable
import dev.kkorolyov.pancake.platform.media.graphic.Image
import dev.kkorolyov.pancake.platform.media.graphic.Renderable
import dev.kkorolyov.pancake.platform.media.graphic.Viewport
import dev.kkorolyov.pancake.platform.registry.Registry
import dev.kkorolyov.pancake.platform.registry.RegistryLoader
import dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory
import dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory.ActionResource
import dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory.AudioResource
import dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory.RenderableResource
import dev.kkorolyov.simpleprops.Properties
import dev.kkorolyov.simplestructs.WeightedDistribution
import java.nio.file.Path
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Supplier
import kotlin.math.sqrt

private val renderables: Registry<String, Renderable> = Registry<String, Renderable>().apply {
	RegistryLoader.fromProperties<Renderable>(Properties(Path.of("config/renderables"))) { ResourceReaderFactory.get(RenderableResource::class.java, it) }.load(this)
}
private val audio: Registry<String, Audio> = Registry<String, Audio>().apply {
	RegistryLoader.fromProperties<Audio>(Properties(Path.of("config/audio"))) { ResourceReaderFactory.get(AudioResource::class.java, it) }.load(this)
}
private val actions: Registry<String, Action> = Registry<String, Action>().apply {
	put("ANIMATE", Action { it.get(Animation::class.java).isActive = true })
	put("STOP_ANIMATE", Action { it.get(Animation::class.java).isActive = false })

	put("TOGGLE_ANIMATE", Action { it.get(Animation::class.java).toggle() })

	put("TOGGLE_SPAWNER", Action { it.get(Spawner::class.java).toggle() })

	RegistryLoader.fromProperties<Action>(Properties(Path.of("config/actions"))) { ResourceReaderFactory.get(ActionResource::class.java, it) }.load(this)
}

private val events: Managed = Managed()

private val healthBarSize: Vector = Vector(1.0, 0.25)

private val playerTransform = Transform(Vector(), randRotation())

private val entities: EntityPool = EntityPool(events).apply {
	val groundGraphic = Graphic(Sprite(CompositeRenderable(renderables.get("ground") as Image), Viewport(3, 2)))
	val boxGraphic = Graphic(renderables.get("box"))
	val sphereGraphic = Graphic(renderables.get("sphere"))

	// Ground
	for (i in -15..15 step 2) {
		for (j in -15..15 step 2) {
			create().apply {
				add(
						Transform(Vector(i.toDouble(), j.toDouble(), -1.0)),
						groundGraphic
				)
			}
		}
	}

	// Wall
	create().apply {
		add(
				Transform(Vector(-3.0, 0.0), randRotation()),
				Bounds(Vector(3.0, 3.0)),
				boxGraphic,
				AudioEmitter().apply {
					enqueue(audio.get("wall"))
				}
		)
	}

	val line = sqrt(200.0).toInt()

	// Boxes
	for (i in 1..line) {
		for (j in 1..line) {
			createObject(Vector(i.toDouble(), -j.toDouble()), Bounds(BOX), boxGraphic)
		}
	}
	// Spheres
	for (i in 1..line) {
		for (j in 1..line) {
			createObject(Vector(i.toDouble(), j.toDouble()), Bounds(RADIUS), sphereGraphic)
		}
	}

	// Camera
	create().apply {
		add(
				Transform(Resources.RENDER_MEDIUM.camera.position),
				Chain(playerTransform.position, 1.0)
		)
	}

	val health = Health(20)

	// Player
	create().also {
		val sprite = Sprite(renderables.get("player") as CompositeRenderable<Image>, Viewport(4, 3), (1e9 / 60).toLong())

		it.add(
				playerTransform,
				Velocity(MAX_SPEED),
				Force(PLAYER_MASS),
				Damping(PLAYER_DAMPING),
				Bounds(BOX, RADIUS),
				Spawner(
						1.0,
						4.0,
						.1,
						WeightedDistribution<Supplier<Iterable<Component>>>().apply {
							Supplier {
								listOf(
										Transform(Vector(1.0, 1.0), randRotation()),
										Velocity(),
										Force(OBJECT_MASS),
										Damping(OBJECT_DAMPING),
										Bounds(BOX, RADIUS),
										sphereGraphic
								)
							}
						}
				).apply { isActive = false },
				Animation(sprite),
				Graphic(sprite),
				Input(true, Input.Handler.fromProperties(Properties(Path.of("config/inputs")), actions)),
				health,
				ActionQueue()
		)

		events.register(EntitiesCollided::class.java) { e ->
			if (it.id == e.collided[0]) {
				get(e.collided[1])?.let {
					it.get(Damage::class.java)?.reset() ?: it.add(Damage(1))
				}
			}
		}

		events.register(EntityCreated::class.java) { e ->
			if (it.id == e.id) audio.get("spawn").state(PLAY)
		}
		events.register(EntityDestroyed::class.java) {
			audio.get("spawn").state(PLAY)
		}
	}
	// Player health
	create().apply {
		add(
				Transform(Vector(0.0, .5, 1.0), playerTransform, false),
				Graphic(HealthBar(health, healthBarSize, Resources.RENDER_MEDIUM))
		)
	}
}

private fun EntityPool.createObject(position: Vector, bounds: Bounds, graphic: Graphic) {
	val transform = Transform(position, randRotation())
	val health = Health(20)

	// Object
	create()
			.add(
					transform,
					Velocity(),
					Force(OBJECT_MASS),
					Damping(OBJECT_DAMPING),
					Chain(null, 0.0, playerTransform.position),
					bounds,
					graphic,
					health
			)
	// Its health bar
	create()
			.add(
					Transform(Vector(0.0, .3, 1.0), transform, false),
					Graphic(HealthBar(health, healthBarSize, Resources.RENDER_MEDIUM)),
					health
			)
}

private fun randRotation(): Vector = ThreadLocalRandom.current().run { Vector(nextDouble(), nextDouble(), nextDouble()) }

/**
 * Executes the game.
 */
fun main() {
	Resources.APPLICATION.execute(
			Config(
					"Killstreek Functional Test",
					"pancake-icon.png",
					640.0,
					640.0
			),
			GameLoop(GameEngine(events, entities))
	)
}
