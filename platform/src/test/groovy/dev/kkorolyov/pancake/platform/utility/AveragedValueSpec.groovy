package dev.kkorolyov.pancake.platform.utility

import spock.lang.Specification

class AveragedValueSpec extends Specification {
	def "throws on <= 0 sample count"() {
		when:
		new AveragedValue(count)

		then:
		thrown(IllegalArgumentException)

		where:
		count << [0, -1, -10]
	}

	def "provides average of samples"() {
		AveragedValue value = new AveragedValue(2)

		when:
		value.add(1)
		value.add(3)

		then:
		value.get() == 2
	}

	def "rotates samples"() {
		AveragedValue value = new AveragedValue(2)

		when:
		value.add(1)
		value.add(3)
		value.add(5)

		then:
		value.get() == 4
	}
}
