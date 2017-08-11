package dev.kkorolyov.killstreek.component

import spock.lang.Shared
import spock.lang.Specification

class HealthSpec extends Specification {
	@Shared int max = Integer.MAX_VALUE
	@Shared int current = 1

	Health health = new Health(max, current)

	def "max-only constructor sets current health to max"() {
		expect:
		new Health(max).getValue().get() == max

		where:
		max << (1..100)
	}

	def "changes by specified amount"() {
		when:
		health.change(amount)

		then:
		health.getValue().get() == (current + amount)

		where:
		amount << (-100..100)
	}

	def "is dead at health <= 0"() {
		when:
		health.getValue().set(value)

		then:
		health.isDead()

		where:
		value << (0..-100)
	}
	def "is superdead at health < 0"() {
		when:
		health.getValue().set(value)

		then:
		health.isSuperDead()

		where:
		value << (-1..-100)
	}
}
