package dev.kkorolyov.pancake.core.component

import dev.kkorolyov.flub.data.WeightedDistribution
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

import java.util.function.Supplier

import static dev.kkorolyov.pancake.test.SpecUtilities.randDouble
import static dev.kkorolyov.pancake.test.SpecUtilities.randVector

class SpawnerSpec extends Specification {
	double minRadius = randDouble()
	double maxRadius = randDouble() + minRadius
	double interval = randDouble()
	Vector3 origin = randVector()

	Position position = new Position(Vector3.of(1, 2, 3))
	WeightedDistribution<Supplier<Iterable<Component>>> templates = new WeightedDistribution()
			.add({ [position] } as Supplier, 1)

	Spawner spawner = new Spawner(minRadius, maxRadius, interval, templates)

	// TODO

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
