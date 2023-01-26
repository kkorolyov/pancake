package dev.kkorolyov.pancake.platform

import spock.lang.Specification

class SuspendSpec extends Specification {
	Suspend suspend = new Suspend()

	def "is not active when no handles"() {
		expect:
		!suspend.active
	}

	def "is active when one handle"() {
		when:
		suspend.add(new Object())

		then:
		suspend.active
	}

	def "is active when many handles"() {
		when:
		suspend.add(new Object())
		suspend.add(new Object())

		then:
		suspend.active
	}

	def "is active when some handles removed"() {
		Object handle = new Object()
		Object other = new Object()

		suspend.add(handle)
		suspend.add(other)

		when:
		suspend.remove(handle)
		suspend.remove(handle)

		then:
		suspend.active
	}

	def "is not active when all handles removed"() {
		Object handle = new Object()
		Object other = new Object()

		suspend.add(handle)
		suspend.add(other)

		when:
		suspend.remove(handle)
		suspend.remove(other)

		then:
		!suspend.active
	}
}
