package dev.kkorolyov.pancake.platform.utility

import spock.lang.Shared
import spock.lang.Specification

class ArgVerifySpec extends Specification {
	@Shared
	static String name = "value"

	static class LessThan extends Specification {
		@Shared
		static int bound = 4

		def "accepts less than value"() {
			expect:
			ArgVerify.lessThan(name, bound, value) == value

			where:
			value << (-bound..bound - 1)
		}
		def "rejects greater than equal value"() {
			when:
			ArgVerify.lessThan(name, bound, value)

			then:
			thrown(IllegalArgumentException)

			where:
			value << (bound..2 * bound)
		}
	}

	static class LessThanEqual extends Specification {
		@Shared
		static int bound = 4

		def "accepts less than equal value"() {
			expect:
			ArgVerify.lessThanEqual(name, bound, value) == value

			where:
			value << (-bound..bound)
		}
		def "rejects greater than value"() {
			when:
			ArgVerify.lessThanEqual(name, bound, value)

			then:
			thrown(IllegalArgumentException)

			where:
			value << (bound + 1..2 * bound)
		}
	}

	static class GreaterThan extends Specification {
		@Shared
		static int bound = 4

		def "accepts greater than value"() {
			expect:
			ArgVerify.greaterThan(name, bound, value) == value

			where:
			value << (bound + 1..bound * 2)
		}
		def "rejects less than equal value"() {
			when:
			ArgVerify.greaterThan(name, bound, value)

			then:
			thrown(IllegalArgumentException)

			where:
			value << (-bound..bound)
		}
	}

	static class GreaterThanEqual extends Specification {
		@Shared
		static int bound = 4

		def "accepts greater than equal value"() {
			expect:
			ArgVerify.greaterThanEqual(name, bound, value) == value

			where:
			value << (bound..bound * 2)
		}
		def "rejects less than value"() {
			when:
			ArgVerify.greaterThanEqual(name, bound, value)

			then:
			thrown(IllegalArgumentException)

			where:
			value << (-bound..bound - 1)
		}
	}

	static class BetweenInclusive extends Specification {
		@Shared
		static int lower = -2
		@Shared
		static int upper = 3

		def "accepts between equal value"() {
			expect:
			ArgVerify.betweenInclusive(name, lower, upper, value) == value

			where:
			value << (lower..upper)
		}
		def "rejects outer value"() {
			when:
			ArgVerify.betweenInclusive(name, lower, upper, value)

			then:
			thrown(IllegalArgumentException)

			where:
			value << (lower * 2..lower - 1) + (upper + 1..upper * 2)
		}
	}
}
