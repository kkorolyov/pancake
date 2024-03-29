package dev.kkorolyov.pancake.platform

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool

import spock.lang.Specification

class PipelineSpec extends Specification {
	GameSystem[] systems = (1..4).collect { Spy(new MockSystem()) }

	EntityPool entities = new EntityPool()
	Suspend suspend = new Suspend()

	Pipeline pipeline = Pipeline.of(systems)

	def setup() {
		entities.create().put(new MockComponent())

		setupPipeline(pipeline)
	}

	def "updates systems with given dt"() {
		when:
		pipeline.update(dt)

		then:
		systems.each {
			1 * it.update(_, dt)
		}

		where:
		dt << (-10..10)
	}

	// strange that only some of these cases need the dt + 1
	def "updates systems with fixed dt to catch up"() {
		long timestep = 1e9 / frequency

		pipeline = setupPipeline(pipeline.fixed(frequency))

		when:
		pipeline.update(dt + 1)

		then:
		systems.each {
			frequency * it.update(_, timestep)
		}

		where:
		frequency << (1..10)
		dt << (1..10).collect { 1e9 as long }
	}
	def "waits to update systems with fixed dt to slow down"() {
		long timestep = 1e9 / frequency

		pipeline = setupPipeline(pipeline.fixed(frequency))

		when:
		pipeline.update(dt + 1)
		pipeline.update(dt)

		then:
		systems.each {
			frequency * it.update(_, timestep)
		}

		where:
		frequency << (1..10)
		dt << (1..10).collect { 1e9 / 2 as long }
	}

	def "updates if suspended"() {
		when:
		suspend.add(new Object())
		pipeline.update(dt)

		then:
		systems.each {
			1 * it.update(_, dt)
		}

		where:
		dt << (-10..10)
	}
	def "does not update if suspendable and suspended"() {
		when:
		pipeline = setupPipeline(pipeline.suspendable())
		suspend.add(new Object())
		pipeline.update(dt)

		then:
		systems.each {
			0 * it._
		}

		where:
		dt << (-10..10)
	}

	private Pipeline setupPipeline(Pipeline pipeline) {
		pipeline.attach(entities, suspend)

		return pipeline
	}

	private static class MockComponent implements Component {}

	private static class MockSystem extends GameSystem {
		protected MockSystem() {
			super(MockComponent)
		}

		@Override
		protected void update(Entity entity, long dt) {}
	}
}
