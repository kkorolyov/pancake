package dev.kkorolyov.killstreek.component

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.simplespecs.SpecUtilities.randLong

class DamageSpec extends Specification {
	@Shared
	int value = 10
	@Shared
	long duration = 100
	Health health = Mock()

	Damage damage = new Damage(value, duration)

	def "no-arg-constructed damage starts expired"() {
		when:
		damage = new Damage()
		damage.apply(health, randLong())

		then:
		0 * health.change(_ as int)
	}

	def "value without duration applies full damage immediately"() {
		when:
		damage.setValue(value)
		damage.apply(health, randLong())

		then:
		1 * health.change(-value)
	}
	def "value with duration applies damage over duration"() {
		Health health = Spy(Health, constructorArgs: [value * 2])

		when:
		while (damage.apply(health, duration / ticks as long)) {}

		then:
		(1..value).each {
			(_..it) * health.change(value / it as int)
		}
		health.getValue().get() == value

		where:
		ticks << (1..100)
	}

	def "returns true when damage applied"() {
		when:
		damage.setValue(value)
		boolean applied = damage.apply(health, randLong())

		then:
		1 * health.change(-value)
		applied
	}
	def "returns false when no damage applied"() {
		when:
		damage.setValue(value, -randLong())
		boolean applied = damage.apply(health, randLong())

		then:
		0 * health.change(_ as int)
		!applied
	}
}
