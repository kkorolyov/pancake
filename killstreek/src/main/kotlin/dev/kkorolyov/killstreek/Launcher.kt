package dev.kkorolyov.killstreek

import dev.kkorolyov.flopple.data.WeightedDistribution
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
import dev.kkorolyov.pancake.core.input.HandlerReader
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
import dev.kkorolyov.pancake.platform.registry.DeferredConverterFactory
import dev.kkorolyov.pancake.platform.registry.DeferredConverterFactory.ActionStrat
import dev.kkorolyov.pancake.platform.registry.DeferredConverterFactory.AudioStrat
import dev.kkorolyov.pancake.platform.registry.DeferredConverterFactory.RenderableStrat
import dev.kkorolyov.pancake.platform.registry.Registry
import dev.kkorolyov.pancake.platform.registry.ResourceReader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Supplier
import kotlin.math.sqrt

private val log: Logger = LoggerFactory.getLogger("main")

private val renderables: Registry<String, Renderable> by lazy {
	Resources.`in`("config/renderables.yaml").orElse(null).use {
		Registry<String, Renderable>().apply {
			load(ResourceReader(DeferredConverterFactory.get(RenderableStrat::class.java)).fromYaml(it))
		}
	}
}
private val audio: Registry<String, Audio> by lazy {
	Resources.`in`("config/audio.yaml").orElse(null).use {
		Registry<String, Audio>().apply {
			load(ResourceReader(DeferredConverterFactory.get(AudioStrat::class.java)).fromYaml(it))
		}
	}
}
private val actions: Registry<String, Action> by lazy {
	Resources.`in`("config/actions.yaml").orElse(null).use {
		Registry<String, Action>().apply {
			put("animate", Action { it.get(Animation::class.java).isActive = true })
			put("stopAnimate", Action { it.get(Animation::class.java).isActive = false })

			put("toggleAnimate", Action { it.get(Animation::class.java).toggle() })

			put("toggleSpawner", Action { it.get(Spawner::class.java).toggle() })

			load(ResourceReader(DeferredConverterFactory.get(ActionStrat::class.java)).fromYaml(it))
		}
	}
}

private val events: Managed = Managed()

private val healthBarSize: Vector = Vector(1.0, 0.25)

private val playerTransform = Transform(Vector(), randRotation())

private val entities: EntityPool by lazy {
	EntityPool(events).apply {
		val groundGraphic = Graphic((renderables.get("ground") as Image).apply { viewport = Viewport(3, 2) })
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
			val sprite =
				Sprite(renderables.get("player") as CompositeRenderable<Image>, Viewport(4, 3), (1e9 / 60).toLong()).apply {
					isActive = false
				}

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
						add(
							Supplier {
								listOf(
									Transform(Vector(1.0, 1.0), randRotation()),
									Velocity(),
									Force(OBJECT_MASS),
									Damping(OBJECT_DAMPING),
									Bounds(BOX, RADIUS),
									sphereGraphic
								)
							},
							1
						)
					}
				).apply { isActive = false },
				Animation(sprite),
				Graphic(sprite),
				Input(
					true,
					Resources.`in`("config/inputs.yaml").orElse(null).use {
						HandlerReader(actions).fromYaml(it)
					}
				),
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

private fun randRotation(): Vector =
	ThreadLocalRandom.current().run { Vector(nextDouble(), nextDouble(), nextDouble()) }

/**
 * Executes the game.
 */
fun main() {
	log.info("Starting thing")
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
