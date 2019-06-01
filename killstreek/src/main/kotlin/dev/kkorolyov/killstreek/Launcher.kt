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
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.event.EntityCreated
import dev.kkorolyov.pancake.platform.event.EntityDestroyed
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.simplestructs.WeightedDistribution
import javafx.application.Application
import javafx.scene.media.MediaPlayer
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Supplier
import dev.kkorolyov.pancake.platform.Launcher as PancakeLauncher

fun main(args: Array<String>) {
	Application.launch(Launcher::class.java, *args)
}

private val rand: ThreadLocalRandom = ThreadLocalRandom.current()
private val healthBarSize: Vector = Vector(1f, .25f)
private val spriteOrientationOffset = Vector(0f, 0f, (-.5 * Math.PI).toFloat())

private fun randRotation(): Vector = Vector(rand.nextFloat(), rand.nextFloat(), rand.nextFloat())

class Launcher : PancakeLauncher(
		LauncherConfig()
				.title("Killstreek Functional Test")
				.size(640f, 640f)
				.unitPixels(Vector(64f, -64f, 1f))
) {
	private val groundGraphic by lazy { Graphic(Sprite(images.get("ground"), Vector(), 3, 2, 0)) }
	private val boxGraphic by lazy { Graphic(Sprite(images.get("box"), spriteOrientationOffset)) }
	private val sphereGraphic by lazy { Graphic(Sprite(images.get("sphere"), spriteOrientationOffset)) }

	private val playerTransform = Transform(Vector(), randRotation())
	private val player: Int by lazy { addPlayer() }

	override fun init() {
		initImages()
		initAudio()
		initActions()
		initEntities()
		initEvents()
		player
	}

	private fun initImages() {
		images.put("config/images")
	}

	private fun initAudio() {
		audio.put("config/audio")
	}

	private fun initActions() {
		actions
				.put("WALK") { it.get(Animation::class.java).isActive = true }
				.put("STOP_WALK") { it.get(Animation::class.java).isActive = false }

				.put("TOGGLE_ANIMATION") { it.get(Animation::class.java).toggle() }

				.put("TOGGLE_SPAWNER") { it.get(Spawner::class.java).toggle() }

				.put("config/actions")
	}

	private fun initEntities() {
		addGround()
		addWall()
		addBoxes(200)
		addSpheres(200)
		addCamera()
	}

	private fun addGround() {
		for (i in -15..15 step 2) {
			for (j in -15..15 step 2) {
				entities.create()
						.add(
								Transform(Vector(i.toFloat(), j.toFloat(), -1f)),
								groundGraphic
						)
			}
		}
	}

	private fun addWall() {
		entities.create()
				.add(
						Transform(Vector(-3f, 0f), randRotation()),
						Bounds(Vector(3f, 3f)),
						boxGraphic,
						AudioEmitter().enqueue(audio.get("wall"))
				)
	}

	private fun addBoxes(count: Int) {
		val line = Math.sqrt(count.toDouble()).toInt()

		for (i in 1..line) {
			for (j in 1..line) {
				addObject(Vector(i.toFloat(), -j.toFloat()), Bounds(Constants.BOX), boxGraphic)
			}
		}
	}

	private fun addSpheres(count: Int) {
		val line = Math.sqrt(count.toDouble()).toInt()

		for (i in 1..line) {
			for (j in 1..line) {
				addObject(Vector(i.toFloat(), j.toFloat()), Bounds(Constants.RADIUS), sphereGraphic)
			}
		}
	}

	private fun addObject(position: Vector, bounds: Bounds, graphic: Graphic) {
		val transform = Transform(position, randRotation())
		val health = Health(20)

		// Object
		entities.create()
				.add(
						transform,
						Velocity(),
						Force(Constants.OBJECT_MASS),
						Damping(Constants.OBJECT_DAMPING),
						Chain(null, 0f, playerTransform.position),
						bounds,
						graphic,
						health
				)
		// Its health bar
		entities.create()
				.add(
						Transform(Vector(0f, .3f, 1f), transform, false),
						Graphic(HealthBar(health, healthBarSize)),
						health,
						Damage()  // To be visible to DamageSystem
				)
	}

	private fun addCamera() {
		entities.create()
				.add(
						Transform(camera.position),
						Chain(playerTransform.position, 1f)
				)
	}

	private fun addPlayer(): Int {
		val sprite = Sprite(images.get("player"), spriteOrientationOffset, 4, 3, (1e9 / 60).toLong())

		val spawner = Spawner(
				1f, 4f, .1f, WeightedDistribution<Supplier<Iterable<Component>>>()
				.add(
						Supplier<Iterable<Component>> {
							listOf(
									Transform(Vector(1f, 1f), randRotation()),
									Velocity(),
									Force(Constants.OBJECT_MASS),
									Damping(Constants.OBJECT_DAMPING),
									Bounds(Constants.BOX, Constants.RADIUS),
									sphereGraphic
							)
						},
						1
				)
		)
		spawner.isActive = false

		val health = Health(100)

		entities.create()
				.add(
						Transform(Vector(0f, .3f, 1f), playerTransform, false),
						Graphic(HealthBar(health, healthBarSize))
				)
		return entities.create()
				.add(
						playerTransform,
						Velocity(Constants.MAX_SPEED),
						Force(Constants.PLAYER_MASS),
						Damping(Constants.PLAYER_DAMPING),
						Bounds(Constants.BOX, Constants.RADIUS),
						spawner,
						Animation(sprite),
						Graphic(sprite),
						Input(true, actions.readKeys("config/keys")),
						health,
						ActionQueue()
				)
				.id
	}

	private fun initEvents() {
		events.register(EntitiesCollided::class.java) {
			if (player == it.collided[0]) {
				entities.get(it.collided[1])?.let {
					it.get(Damage::class.java)?.reset() ?: it.add(Damage(1))
				}
			}
		}

		events.register(EntityCreated::class.java) {
			if (player == it.id) MediaPlayer(audio.get("spawn")).play()
		}
		events.register(EntityDestroyed::class.java) {
			MediaPlayer(audio.get("spawn")).play()
		}
	}
}
