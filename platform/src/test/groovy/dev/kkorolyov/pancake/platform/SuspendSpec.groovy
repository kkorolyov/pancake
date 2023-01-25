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
		suspend.add(new Suspend.Handle() {})

		then:
		suspend.active
	}

	def "is active when many handles"() {
		when:
		suspend.add(new Suspend.Handle() {})
		suspend.add(new Suspend.Handle() {})

		then:
		suspend.active
	}

	def "is active when some handles removed"() {
		Suspend.Handle handle = new Suspend.Handle() {}
		Suspend.Handle other = new Suspend.Handle() {}

		suspend.add(handle)
		suspend.add(other)

		when:
		suspend.remove(handle)
		suspend.remove(handle)

		then:
		suspend.active
	}

	def "is not active when all handles removed"() {
		Suspend.Handle handle = new Suspend.Handle() {}
		Suspend.Handle other = new Suspend.Handle() {}

		suspend.add(handle)
		suspend.add(other)

		when:
		suspend.remove(handle)
		suspend.remove(other)

		then:
		!suspend.active
	}
}
