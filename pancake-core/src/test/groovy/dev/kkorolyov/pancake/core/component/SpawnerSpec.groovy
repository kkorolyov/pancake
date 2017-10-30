package dev.kkorolyov.pancake.core.component

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.math.WeightedDistribution

import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Supplier

import static dev.kkorolyov.pancake.platform.SpecUtilities.randFloat

class SpawnerSpec extends Specification {
	@Shared float minRadius = 12.43
	@Shared float maxRadius = 24.1
	@Shared float interval = 0.5
	@Shared Vector origin = new Vector(5, 4, 3)

	Supplier<Iterable<Component>> templateSupplier = Mock()
	WeightedDistribution<Supplier<Iterable<Component>>> templates = Mock() {
		it.get() >> templateSupplier
	}

	Spawner spawner = new Spawner(minRadius, maxRadius, interval, templates)

	def "moves clone's transform to origin"() {
		templateSupplier.get() >> [new Transform(new Vector())]	// All 0 for no randomization

		expect:
		(spawner.spawn(origin)[0] as Transform).position == origin
	}

	def "randomizes all non-zero transform position components"() {
		templateSupplier.get() >> [new Transform(new Vector(position))]

		expect:
		Vector clonePosition = (spawner.spawn(new Vector())[0] as Transform).position	// Easier 0 component comparison
		(position.getX() != 0) ? (position.getX() != clonePosition.getX()) : (position.getX() == clonePosition.getX())
		(position.getY() != 0) ? (position.getY() != clonePosition.getY()) : (position.getY() == clonePosition.getY())
		(position.getZ() != 0) ? (position.getZ() != clonePosition.getZ()) : (position.getZ() == clonePosition.getZ())

		where:
		position << [new Vector(randFloat(), 0, 0), new Vector(0, randFloat(), 0), new Vector(0, 0 , randFloat())]
	}

	@Ignore	// TODO This
	def "clone's transform's distance from origin between min/max radii"() {
		templateSupplier.get()>> [new Transform(new Vector(1, 1, 1))]

		expect:
		float distance = (spawner.spawn(origin)[0] as Transform).position.distance(origin)
		distance >= minRadius
		distance <= maxRadius
	}
}
