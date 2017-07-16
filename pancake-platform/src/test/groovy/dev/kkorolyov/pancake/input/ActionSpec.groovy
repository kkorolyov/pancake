package dev.kkorolyov.pancake.input

import dev.kkorolyov.pancake.entity.Entity

import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Consumer

import static dev.kkorolyov.pancake.SpecUtilities.getField
import static dev.kkorolyov.pancake.SpecUtilities.randFloat

class ActionSpec extends Specification {
	@Shared String name = "SomeName"
	@Shared Consumer<Entity> e1 = {e -> 1}
	@Shared Consumer<Entity> e2 = {e -> 2}
	@Shared Consumer<Entity> e3 = {e -> 3}

	Action action = new Action(name, e1, e2, e3)

	def "action from other actions uses start events of those actions"() {
		Action a1 = new Action(name, e1)
		Action a2 = new Action(name, e2)
		Action a3 = new Action(name, e3)

		when:
		action = new Action(name, a1, a2, a3)

		then:
		getField("start", action) == getField("start", a1)
		getField("hold", action) == getField("start", a2)
		getField("stop", action) == getField("start", a3)
	}

	def "returns start event when first signalled"() {
		expect:
		action.signal(true, randFloat()) == e1
	}

	def "returns no event when signalled within some time threshold after first signal"() {
		when:
		action.signal(true, randFloat())

		then:
		action.signal(true, time) == null

		where:
		time << [getHoldThreshold() * 0.99999 as float,
				getHoldThreshold() * 0.9999 as float,
				getHoldThreshold() * 0.999 as float]
	}
	def "returns hold event when signalled at/after some time threshold after first signal"() {
		when:
		action.signal(true, randFloat())

		then:
		action.signal(true, time) == e2

		where:
		time << [getHoldThreshold(),
				getHoldThreshold() * 2 as float,
				getHoldThreshold() * 3 as float]
	}
	def "sums times of each signal after first signal"() {
		when:
		action.signal(true, randFloat())

		then:
		action.signal(true, getHoldThreshold() / 2 as float) == null
		action.signal(true, getHoldThreshold() / 2 as float) == e2
	}
	def "counts signal times only after first signal"() {
		when:
		action.signal(true, getHoldThreshold())

		then:
		action.signal(true, getHoldThreshold() * 0.99999f as float) == null
	}

	def "returns no event when unsignalled when not yet signalled"() {
		expect:
		action.signal(false, randFloat()) == null
	}
	def "returns stop event when unsignalled after first signal"() {
		when:
		action.signal(true, randFloat()) == null

		then:
		action.signal(false, randFloat()) == e3
	}
	def "returns stop event when unsignalled after hold signal"() {
		when:
		action.signal(true, randFloat())
		action.signal(true, getHoldThreshold())

		then:
		action.signal(false, randFloat()) == e3
	}

	float getHoldThreshold() {
		return getField("HOLD_THRESHOLD", Action) as float
	}
}
