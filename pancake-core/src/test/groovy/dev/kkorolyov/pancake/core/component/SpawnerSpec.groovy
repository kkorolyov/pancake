package dev.kkorolyov.pancake.core.component

import dev.kkorolyov.flopple.data.WeightedDistribution
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector

import spock.lang.Specification

import java.util.function.Supplier

import static dev.kkorolyov.pancake.platform.SpecUtilities.randDouble
import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector

class SpawnerSpec extends Specification {
	double minRadius = randDouble()
	double maxRadius = randDouble() + minRadius
	double interval = randDouble()
	Vector origin = randVector()

	Vector position = Spy(new Vector(1, 1, 1))
	Transform transform = new Transform(position)
	WeightedDistribution<Supplier<Iterable<Component>>> templates = new WeightedDistribution()
			.add({ [transform] } as Supplier, 1)

	Spawner spawner = new Spawner(minRadius, maxRadius, interval, templates)

	def "modifies clone's transform"() {
		when:
		spawner.spawn(origin)

		then:
		2 * position.setX(_)
		2 * position.setY(_)
		2 * position.setZ(_)
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
		expect:
		spawner.spawn(origin, interval / 2) == null
	}
	def "spawns if interval elapsed"() {
		expect:
		spawner.spawn(origin, interval) != null
	}

	def "does nothing if inactive"() {
		spawner.setActive(false)

		expect:
		spawner.spawn(origin, interval) == null
	}
}
