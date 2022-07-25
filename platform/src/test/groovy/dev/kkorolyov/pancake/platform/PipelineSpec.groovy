package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.entity.EntityPool

import spock.lang.Specification

class PipelineSpec extends Specification {
	GameSystem[] systems = (1..4).collect { Mock(GameSystem) }

	Pipeline pipeline = new Pipeline(systems)

	def setup() {
		pipeline.attach(new EntityPool())
	}

	def "updates systems with given dt"() {
		when:
		pipeline.update(dt)

		then:
		systems.each {
			1 * it.before()
		}

		where:
		dt << (-10..10)
	}

	// strange that only some of these cases need the dt + 1
	def "updates systems with fixed dt to catch up"() {
		long timestep = 1e9 / frequency

		pipeline = pipeline.withFrequency(frequency)
		pipeline.attach(new EntityPool())

		when:
		pipeline.update(dt + 1)

		then:
		systems.each {
			frequency * it.before()
		}

		where:
		frequency << (1..10)
		dt << (1..10).collect { 1e9 as long }
	}
	def "waits to update systems with fixed dt to slow down"() {
		long timestep = 1e9 / frequency

		pipeline = pipeline.withFrequency(frequency)
		pipeline.attach(new EntityPool())

		when:
		pipeline.update(dt + 1)
		pipeline.update(dt)

		then:
		systems.each {
			frequency * it.before()
		}

		where:
		frequency << (1..10)
		dt << (1..10).collect { 1e9 / 2 as long }
	}
}
