package dev.kkorolyov.pancake.core.component

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.simplestructs.WeightedDistribution

import spock.lang.Specification

import java.util.function.Supplier

import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector
import static dev.kkorolyov.simplespecs.SpecUtilities.randDouble 

class SpawnerSpec extends Specification {
	double minRadius = randDouble()
	double maxRadius = randDouble() + minRadius
	double interval = randDouble()
	Vector origin = randVector()

	Vector position = Spy(new Vector(1, 1, 1))
	Transform transform = Mock() {
		getPosition() >> position
	}
	Supplier<Iterable<Component>> templateSupplier = Mock() {
		get() >> [transform]
	}
	WeightedDistribution<Supplier<Iterable<Component>>> templates = Mock() {
		get() >> templateSupplier
	}

	Spawner spawner = new Spawner(minRadius, maxRadius, interval, templates)

	def "modifies clone's transform"() {
		when:
		spawner.spawn(origin)

		then:
		1 * position.setX(_)
		1 * position.setY(_)
		1 * position.setZ(_)
	}
	def "adds origin to clone's transform"() {
		when:
		spawner.spawn(origin)

		then:
		1 * position.add(origin)
	}
	def "clone's transform is positioned between min/max radii around origin"() {
		when:
		spawner.spawn(origin)

		then:
		position.distance(origin) >= minRadius
		position.distance(origin) <= maxRadius
	}

	def "does nothing if interval not yet elapsed"() {
		when:
		def result = spawner.spawn(origin, interval / 2)

		then:
		result == null
		0 * templateSupplier.get()
	}
	def "spawns if interval elapsed"() {
		when:
		spawner.spawn(origin, interval)

		then:
		1 * templateSupplier.get() >> [transform]
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
