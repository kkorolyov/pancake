package dev.kkorolyov.pancake.core.component

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.math.WeightedDistribution

import spock.lang.Specification

import java.util.function.Supplier

import static dev.kkorolyov.pancake.platform.SpecUtilities.randFloat
import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector

class SpawnerSpec extends Specification {
	float minRadius = randFloat()
	float maxRadius = randFloat() + minRadius
	float interval = randFloat()
	Vector origin = randVector()
	Supplier<Iterable<Component>> templateSupplier = Mock()
	WeightedDistribution<Supplier<Iterable<Component>>> templates = Mock() {
		get() >> templateSupplier
	}

	Spawner spawner = new Spawner(minRadius, maxRadius, interval, templates)

	def "modifies supplied clone components"() {
		Vector position = Mock() {
			getX() >> 1
			getY() >> 1
			getZ() >> 1
		}
		templateSupplier.get() >> [new Transform(position)]

		when:
		spawner.spawn(origin)

		then:
		1 * position.setX(_)
		1 * position.setY(_)
		1 * position.setZ(_)
	}

	def "moves clone's transform to origin"() {
		templateSupplier.get() >> [new Transform(new Vector())]

		when:
		Transform clone = spawner.spawn(origin)[0] as Transform

		then:
		clone.position == origin
	}

	def "clone is positioned between min/max radii around origin"() {
		templateSupplier.get() >> [new Transform(new Vector(1, 1, 1))]

		when:
		Transform clone = spawner.spawn(origin)[0] as Transform

		then:
		clone.position.distance(origin) >= minRadius
		clone.position.distance(origin) <= maxRadius
	}

	def "does nothing if interval not yet elapsed"() {
		when:
		def result = spawner.spawn(origin, (interval / 2) as float)

		then:
		result == null
		0 * templateSupplier.get()
	}
	def "spawns if interval elapsed"() {
		when:
		spawner.spawn(origin, interval)

		then:
		1 * templateSupplier.get() >> []
	}

	def "does nothing if inactive"() {
		when:
		spawner.setActive(false)
		def result = spawner.spawn(origin, interval)

		then:
		result == null
		0 * templateSupplier.get()
	}
}
