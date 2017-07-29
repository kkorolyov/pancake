package dev.kkorolyov.killstreek.component

import spock.lang.Shared
import spock.lang.Specification

class HealthSpec extends Specification {
	@Shared int max = 1
	@Shared int current = 1

	Health health = new Health(max, current)

	def "max-only constructor sets current health to max"() {
		expect:
		new Health(max).getCurrent() == max

		where:
		max << (1..100)
	}

	def "accepted positive values decrement current health"() {
		when:
		health.accept(damage)

		then:
		health.getCurrent() == (current - damage)

		where:
		damage << (0..100)
	}
	def "accepted negative values increment current health"() {
		when:
		health.accept(-1 * heal)

		then:
		health.getCurrent() == (current + heal)

		where:
		heal << (0..100)
	}

	def "sets <= 0 max health to 1"() {
		when:
		health.setMax(badValue)

		then:
		health.getMax() == 1

		where:
		badValue << (0..-100)
	}

	def "sets < 0 current health to 0"() {
		when:
		health.setCurrent(badValue)

		then:
		health.getCurrent() == 0

		where:
		badValue << (-1..-100)
	}
	def "sets > max current health to max"() {
		when:
		health.setCurrent(badValue)

		then:
		health.getCurrent() == max

		where:
		badValue << ((max + 1)..(max + 100))
	}
}
