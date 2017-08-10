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

	def "changes by specified amount"() {
		when:
		health.setMax(Integer.MAX_VALUE)
		health.change(amount)

		then:
		health.getCurrent() == (current + amount)

		where:
		amount << (-100..100)
	}

	def "sets <= 0 max health to 1"() {
		when:
		health.setMax(badValue)

		then:
		health.getMax() == 1

		where:
		badValue << (0..-100)
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
