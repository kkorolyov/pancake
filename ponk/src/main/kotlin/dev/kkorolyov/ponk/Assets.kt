package dev.kkorolyov.ponk

import dev.kkorolyov.pancake.core.component.ActionQueue
import dev.kkorolyov.pancake.core.component.Bounds
import dev.kkorolyov.pancake.core.component.Input
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.core.component.media.Graphic
import dev.kkorolyov.pancake.core.component.movement.Damping
import dev.kkorolyov.pancake.core.component.movement.Force
import dev.kkorolyov.pancake.core.component.movement.Mass
import dev.kkorolyov.pancake.core.component.movement.Velocity
import dev.kkorolyov.pancake.core.component.movement.VelocityCap
import dev.kkorolyov.pancake.core.input.HandlerReader
import dev.kkorolyov.pancake.platform.Resources
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.event.EventLoop
import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors
import dev.kkorolyov.pancake.platform.media.graphic.Renderable
import dev.kkorolyov.pancake.platform.media.graphic.shape.Shape.Color
import dev.kkorolyov.pancake.platform.registry.DeferredConverterFactory
import dev.kkorolyov.pancake.platform.registry.Registry
import dev.kkorolyov.pancake.platform.registry.ResourceReader

val actions: Registry<String, Action> = Resources.inStream("actions.yaml").use {
	Registry<String, Action>().apply {
		load(
			ResourceReader(DeferredConverterFactory.get(DeferredConverterFactory.ActionStrat::class.java)).fromYaml(
				it
			)
		)
	}
}

val events: EventLoop.Broadcasting = EventLoop.Broadcasting()

val velocityCap: Vector3 = Vectors.create(20.0, 20.0, 20.0)
val damping: Vector3 = Vectors.create(0.0, 0.0, 0.0)
const val paddleMass = 1e-2
const val ballMass = 1e-9

val paddleSize: Vector3 = Vectors.create(1.0, 4.0, 0.0)
val ballSize: Vector3 = Vectors.create(1.0, 1.0, 0.0)
val goalSize: Vector3 = Vectors.create(2.0, 10.0, 0.0)
val netSize: Vector3 = Vectors.create(10.0, 2.0, 0.0)

val paddleSprite: Renderable = Resources.RENDER_MEDIUM.box.apply {
	fill = Color.BLACK
	size.set(paddleSize)
}
val ballSprite: Renderable = Resources.RENDER_MEDIUM.box.apply {
	fill = Color.GRAY
	size.set(ballSize)
}
val goalSprite: Renderable = Resources.RENDER_MEDIUM.box.apply {
	fill = Color.BLUE
	size.set(goalSize)
}
val netSprite: Renderable = Resources.RENDER_MEDIUM.box.apply {
	fill = Color.RED
	size.set(netSize)
}

val entities: EntityPool = EntityPool(events)

val player = entities.create().apply {
	add(
		Graphic(paddleSprite),
		Bounds(paddleSize),
		Transform(Vectors.create(-4.0, 0.0, 0.0)),
		Velocity(Vectors.create(0.0, 0.0, 0.0)),
		VelocityCap(velocityCap),
		Damping(damping),
		Force(Vectors.create(0.0, 0.0, 0.0)),
		Mass(paddleMass),
		ActionQueue()
	)
	Resources.inStream("input.yaml").use {
		add(Input(false, HandlerReader(actions).fromYaml(it)))
	}
}

val opponent = entities.create().apply {
	add(
		Graphic(paddleSprite),
		Bounds(paddleSize),
		Transform(Vectors.create(4.0, 0.0, 0.0)),
		Velocity(Vectors.create(0.0, 0.0, 0.0)),
		VelocityCap(velocityCap),
		Damping(damping),
		Force(Vectors.create(0.0, 0.0, 0.0)),
		Mass(paddleMass)
	)
}

val ball = entities.create().apply {
	add(
		Graphic(ballSprite),
		Bounds(ballSize),
		Transform(Vectors.create(0.0, 0.0, 0.0)),
		Velocity(Vectors.create(0.0, 0.0, 0.0)),
		VelocityCap(Vectors.create(20.0, 20.0, 0.0)),
		Force(Vectors.create(0.0, 0.0, 0.0)),
		Mass(ballMass),
		ActionQueue()
	)
	Resources.inStream("ballInput.yaml").use {
		add(Input(false, HandlerReader(actions).fromYaml(it)))
	}
}

val goalPlayer = entities.create().apply {
	add(
		Bounds(goalSize),
		Graphic(goalSprite),
		Transform(Vectors.create(-6.0, 0.0, 0.0))
	)
}
val goalOpponent = entities.create().apply {
	add(
		Bounds(goalSize),
		Graphic(goalSprite),
		Transform(Vectors.create(6.0, 0.0, 0.0))
	)
}

val top = entities.create().apply {
	add(
		Bounds(netSize),
		Graphic(netSprite),
		Transform(Vectors.create(0.0, 6.0, 0.0))
	)
}
val bottom = entities.create().apply {
	add(
		Bounds(netSize),
		Graphic(netSprite),
		Transform(Vectors.create(0.0, -6.0, 0.0))
	)
}

val helpText = entities.create().apply {
	add(
		Transform(Vectors.create(-0.5, -4.0, 1.0)),
		Graphic(Resources.RENDER_MEDIUM.text.apply {
			value = "Press SPACE to reset"
		})
	)
}
