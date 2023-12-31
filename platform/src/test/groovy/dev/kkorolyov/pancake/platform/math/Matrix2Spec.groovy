package dev.kkorolyov.pancake.platform.math

import spock.lang.Specification

class Matrix2Spec extends Specification {
	def "computes determinant"() {
		expect:
		Matrix2.determinant(matrix) == determinant

		where:
		matrix << [
				Matrix2.identity(),
				Matrix2.of(
						1, 5,
						8, 2
				)
		]
		determinant << [1, -38]
	}
}
