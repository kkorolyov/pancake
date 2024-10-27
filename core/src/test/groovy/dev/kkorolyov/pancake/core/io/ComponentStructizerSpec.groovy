package dev.kkorolyov.pancake.core.io

import dev.kkorolyov.pancake.core.component.Animator
import dev.kkorolyov.pancake.core.component.Damping
import dev.kkorolyov.pancake.core.component.Force
import dev.kkorolyov.pancake.core.component.Mass
import dev.kkorolyov.pancake.core.component.Path
import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.core.component.Velocity
import dev.kkorolyov.pancake.core.component.limit.VelocityLimit
import dev.kkorolyov.pancake.core.component.tag.Collidable
import dev.kkorolyov.pancake.core.component.tag.Correctable
import dev.kkorolyov.pancake.platform.animation.Choreography
import dev.kkorolyov.pancake.platform.animation.Timeline
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Ignore
import spock.lang.Specification

class ComponentStructizerSpec extends Specification {
	def structizer = new ComponentStructizer()

	@Ignore
	def "toStructs ActionQueue"() {
		// TODO
	}
	@Ignore
	def "fromStructs ActionQueue"() {
		// TODO
	}
	@Ignore
	def "full-circles ActionQueue"() {
		// TODO
	}

	def "toStructs Animator"() {
		def choreography = new Choreography().with {
			it.put("someRole", new Timeline())
			it
		}
		def otherChoreography = new Choreography().with {
			it.put("otherRole", new Timeline())
			it.put("lastRole", new Timeline())
			it
		}

		expect:
		structizer.toStruct(new Animator().with {
			it.put(choreography.get("someRole"), Animator.Type.ONCE)
			it.put(otherChoreography.get("otherRole"), Animator.Type.LOOP)
			it.put(otherChoreography.get("lastRole"), Animator.Type.RESET)
			it
		}).get() == [
				TODO: [
						someRole: [
								offset: 0,
								type: "ONCE"
						],
						otherRole: [
								offset: 0,
								type: "LOOP"
						],
						lastRole: [
								offset: 0,
								type: "RESET"
						]
				]
		]
	}
	def "fromStructs Animator"() {
		when:
		def result = structizer.fromStruct(Animator, [
				TODO: [
						someRole: [
								offset: 0,
								type: "ONCE"
						],
						otherRole: [
								offset: 0,
								type: "LOOP"
						],
						lastRole: [
								offset: 0,
								type: "RESET"
						]
				]
		]).orElseThrow().toList()

		then:
		// TODO
		result == []
//		result[0].getKey().key() == "someRole"
//		result[0].getValue().playback().getOffset() == 0
//		result[0].getValue().type() == Animator.Type.ONCE
//
//		result[1].getKey().key() == "otherRole"
//		result[1].getValue().playback().getOffset() == 0
//		result[1].getValue().type() == Animator.Type.LOOP
//
//		result[2].getKey().key() == "lastRole"
//		result[2].getValue().playback().getOffset() == 0
//		result[2].getValue().type() == Animator.Type.RESET
	}
	def "full-circles Animator"() {
		def choreography = new Choreography().with {
			it.put("someRole", new Timeline())
			it
		}
		def otherChoreography = new Choreography().with {
			it.put("otherRole", new Timeline())
			it.put("lastRole", new Timeline())
			it
		}

		when:
		def result = structizer.toStruct(new Animator().with {
			it.put(choreography.get("someRole"), Animator.Type.ONCE)
			it.put(otherChoreography.get("otherRole"), Animator.Type.LOOP)
			it.put(otherChoreography.get("lastRole"), Animator.Type.RESET)
			it
		}).flatMap { structizer.fromStruct(Animator, it) }
				.get().toList()

		then:
		// TODO
		result == []
//		result[0].getKey().key() == "someRole"
//		result[0].getValue().playback().getOffset() == 0
//		result[0].getValue().type() == Animator.Type.ONCE
//
//		result[1].getKey().key() == "otherRole"
//		result[1].getValue().playback().getOffset() == 0
//		result[1].getValue().type() == Animator.Type.LOOP
//
//		result[2].getKey().key() == "lastRole"
//		result[2].getValue().playback().getOffset() == 0
//		result[2].getValue().type() == Animator.Type.RESET
	}

	@Ignore
	def "toStructs Bounds"() {
		// TODO
	}
	@Ignore
	def "fromStructs Bounds"() {
		// TODO
	}
	@Ignore
	def "full-circles Bounds"() {
		// TODO
	}

	def "toStructs empty Path"() {
		expect:
		structizer.toStruct(new Path(strength, proximity, snapStrategy)) == Optional.of([
				strength: strength,
				proximity: proximity,
				snapStrategy: snapStrategy.name(),
				steps: []
		])

		where:
		strength << [0.0d, 0.5d, 45.2d]
		proximity << [85.2d, 0.4d, 33.3d]
		snapStrategy << [Path.SnapStrategy.ALL, Path.SnapStrategy.LAST, Path.SnapStrategy.NONE]
	}
	def "toStructs Path with steps"() {
		def step = [strength, proximity, strength]

		expect:
		structizer.toStruct(new Path(strength, proximity, snapStrategy).with {
			it.add(Vector3.of(*step))
			it
		}) == Optional.of([
				strength: strength,
				proximity: proximity,
				snapStrategy: snapStrategy.name(),
				steps: [step]
		])

		where:
		strength << [0.0d, 0.5d, 45.2d]
		proximity << [85.2d, 0.4d, 33.3d]
		snapStrategy << [Path.SnapStrategy.ALL, Path.SnapStrategy.LAST, Path.SnapStrategy.NONE]
	}

	def "fromStructs empty Path"() {
		when:
		def result = structizer.fromStruct(Path, [
				strength: strength,
				proximity: proximity,
				snapStrategy: snapStrategy.name()
		]).get()

		then:
		result.strength == strength
		result.proximity == proximity
		result.snapStrategy == snapStrategy
		!result.hasNext()

		where:
		strength << [0.0d, 0.5d, 45.2d]
		proximity << [85.2d, 0.4d, 33.3d]
		snapStrategy << [Path.SnapStrategy.ALL, Path.SnapStrategy.LAST, Path.SnapStrategy.NONE]
	}
	def "fromStructs Path with steps"() {
		def step = [strength, proximity, strength]

		when:
		def result = structizer.fromStruct(Path, [
				strength: strength,
				proximity: proximity,
				snapStrategy: snapStrategy.name(),
				steps: [step]
		]).get()

		then:
		result.strength == strength
		result.proximity == proximity
		result.snapStrategy == snapStrategy
		result.next() == Vector3.of(*step)

		where:
		strength << [0.0d, 0.5d, 45.2d]
		proximity << [85.2d, 0.4d, 33.3d]
		snapStrategy << [Path.SnapStrategy.ALL, Path.SnapStrategy.LAST, Path.SnapStrategy.NONE]
	}

	def "full-circles Path"() {
		def step = [strength, proximity, strength]

		when:
		def result = structizer.toStruct(new Path(strength, proximity, snapStrategy).with {
			it.add(Vector3.of(*step))
			it
		}).flatMap { structizer.fromStruct(Path, it) }
				.get()

		then:
		result.strength == strength
		result.proximity == proximity
		result.snapStrategy == snapStrategy
		result.next() == Vector3.of(*step)

		where:
		strength << [0.0d, 0.5d, 45.2d]
		proximity << [85.2d, 0.4d, 33.3d]
		snapStrategy << [Path.SnapStrategy.ALL, Path.SnapStrategy.LAST, Path.SnapStrategy.NONE]
	}

	def "toStructs Collidable"() {
		expect:
		structizer.toStruct(new Collidable(priority)) == Optional.of(priority)

		where:
		priority << (0..5)
	}
	def "fromStructs Collidable"() {
		expect:
		structizer.fromStruct(Collidable, priority).map { it.priority } == Optional.of(priority)

		where:
		priority << (0..5)
	}
	def "full-circles Collidable"() {
		expect:
		structizer.toStruct(new Collidable(priority)).flatMap { structizer.fromStruct(Collidable, it) }.map { it.priority } == Optional.of(priority)

		where:
		priority << (0..5)
	}

	def "toStructs Correctable"() {
		expect:
		structizer.toStruct(new Correctable(priority)) == Optional.of(priority)

		where:
		priority << (0..5)
	}
	def "fromStructs Correctable"() {
		expect:
		structizer.fromStruct(Correctable, priority).map { it.priority } == Optional.of(priority)

		where:
		priority << (0..5)
	}
	def "full-circles Correctable"() {
		expect:
		structizer.toStruct(new Correctable(priority)).flatMap { structizer.fromStruct(Correctable, it) }.map { it.priority } == Optional.of(priority)

		where:
		priority << (0..5)
	}

	def "toStructs Mass"() {
		expect:
		structizer.toStruct(new Mass(value)) == Optional.of(value)

		where:
		value << (0..5).collect { it.doubleValue() }
	}
	def "fromStructs Mass"() {
		expect:
		structizer.fromStruct(Mass, value).map { it.value } == Optional.of(value)

		where:
		value << (0..5).collect { it.doubleValue() }
	}
	def "full-circles Mass"() {
		expect:
		structizer.toStruct(new Mass(value)).flatMap { structizer.fromStruct(Mass, it) }.map { it.value } == Optional.of(value)

		where:
		value << (0..5).collect { it.doubleValue() }
	}

	def "toStructs VelocityLimit"() {
		expect:
		structizer.toStruct(new VelocityLimit(linear, angular)) == Optional.of([linear, angular])

		where:
		linear << (0..5).collect { it.doubleValue() }
		angular << (5..0).collect { it.doubleValue() }
	}
	def "fromStructs VelocityLimit"() {
		expect:
		structizer.fromStruct(VelocityLimit, [linear, angular]).map { [it.linear, it.angular] } == Optional.of([linear, angular])

		where:
		linear << (0..5).collect { it.doubleValue() }
		angular << (5..0).collect { it.doubleValue() }
	}
	def "full-circles VelocityLimit"() {
		expect:
		structizer.toStruct(new VelocityLimit(linear, angular)).flatMap { structizer.fromStruct(VelocityLimit, it) }.map { [it.linear, it.angular] } == Optional.of([linear, angular])

		where:
		linear << (0..5).collect { it.doubleValue() }
		angular << (5..0).collect { it.doubleValue() }
	}

	def "toStructs Damping"() {
		expect:
		structizer.toStruct(new Damping(Vector3.of(x, y, z), Vector3.of(z, y, x))) == Optional.of([[x.doubleValue(), y.doubleValue(), z.doubleValue()], [z.doubleValue(), y.doubleValue(), x.doubleValue()]])

		where:
		x << (0..1)
		y << (1..0)
		z << (0..1)
	}
	def "fromStructs Damping"() {
		expect:
		structizer.fromStruct(Damping, [[x, y, z], [z, y, x]]).map { [it.linear, it.angular] } == Optional.of([Vector3.of(x, y, z), Vector3.of(z, y, x)])

		where:
		x << (0..1)
		y << (1..0)
		z << (0..1)
	}
	def "full-circles Damping"() {
		def value = new Damping(Vector3.of(x, y, z), Vector3.of(z, y, x))

		expect:
		structizer.toStruct(value).flatMap { structizer.fromStruct(Damping, it) }.map { [it.linear, it.angular] } == Optional.of([value.linear, value.angular])

		where:
		x << (0..1)
		y << (1..0)
		z << (0..1)
	}

	def "toStructs Force"() {
		expect:
		structizer.toStruct(new Force().with {
			it.value.set(Vector3.of(x, y, z))
			it.offset.set(Vector3.of(z, y, x))
			it
		}) == Optional.of([[x.doubleValue(), y.doubleValue(), z.doubleValue()], [z.doubleValue(), y.doubleValue(), x.doubleValue()]])

		where:
		x << (1..4)
		y << (4..1)
		z << (5..8)
	}
	def "fromStructs Force"() {
		expect:
		structizer.fromStruct(Force, [[x, y, z], [z, y, x]]).map { [it.value, it.offset] } == Optional.of([Vector3.of(x, y, z), Vector3.of(z, y, x)])

		where:
		x << (1..4)
		y << (4..1)
		z << (5..8)
	}
	def "full-circles Force"() {
		def value = new Force().with {
			it.value.set(Vector3.of(x, y, z))
			it.offset.set(Vector3.of(z, y, x))
			it
		}

		expect:
		structizer.toStruct(value).flatMap { structizer.fromStruct(Force, it) }.map { [it.value, it.offset] } == Optional.of([value.value, value.offset])

		where:
		x << (1..4)
		y << (4..1)
		z << (5..8)
	}

	def "toStructs Transform"() {
		expect:
		structizer.toStruct(new Transform().with {
			it.translation.set(Vector3.of(x, y, z))
			it.scale.set(Vector3.of(z, y, x))
			it
		}) == Optional.of([
				translation: [x.doubleValue(), y.doubleValue(), z.doubleValue()],
				rotation: [
						1, 0, 0, 0,
						0, 1, 0, 0,
						0, 0, 1, 0,
						0, 0, 0, 1
				].collect { it.doubleValue() },
				scale: [z.doubleValue(), y.doubleValue(), x.doubleValue()]
		])

		where:
		x << (1..4)
		y << (4..1)
		z << (5..8)
	}
	def "fromStructs Transform"() {
		when:
		def result = structizer.fromStruct(Transform, [
				translation: [x, y, z],
				scale: [z, y, x]
		]).get()

		then:
		result.translation == Vector3.of(x, y, z)
		result.rotation == Matrix4.of()
		result.scale == Vector3.of(z, y, x)

		where:
		x << (1..4)
		y << (4..1)
		z << (5..8)
	}
	def "full-circles Transform"() {
		when:
		def result = structizer.toStruct(new Transform().with {
			it.translation.set(Vector3.of(x, y, z))
			it.scale.set(Vector3.of(z, y, x))
			it
		}).flatMap { structizer.fromStruct(Transform, it) }
				.get()

		then:
		result.translation == Vector3.of(x, y, z)
		result.rotation == Matrix4.of()
		result.scale == Vector3.of(z, y, x)

		where:
		x << (1..4)
		y << (4..1)
		z << (5..8)
	}

	def "toStructs Velocity"() {
		expect:
		structizer.toStruct(new Velocity().with {
			it.linear.set(Vector3.of(x, y, z))
			it.angular.set(Vector3.of(z, y, x))
			it
		}) == Optional.of([[x.doubleValue(), y.doubleValue(), z.doubleValue()], [z.doubleValue(), y.doubleValue(), x.doubleValue()]])

		where:
		x << (1..4)
		y << (4..1)
		z << (5..8)
	}
	def "fromStructs Velocity"() {
		expect:
		structizer.fromStruct(Velocity, [[x, y, z], [z, y, x]]).map { [it.linear, it.angular] } == Optional.of([Vector3.of(x, y, z), Vector3.of(z, y, x)])

		where:
		x << (1..4)
		y << (4..1)
		z << (5..8)
	}
	def "full-circles Velocity"() {
		def value = new Velocity().with {
			it.linear.set(Vector3.of(x, y, z))
			it.angular.set(Vector3.of(z, y, x))
			it
		}

		expect:
		structizer.toStruct(value).flatMap { structizer.fromStruct(Velocity, it) }.map { [it.linear, it.angular] } == Optional.of([value.linear, value.angular])

		where:
		x << (1..4)
		y << (4..1)
		z << (5..8)
	}
}
