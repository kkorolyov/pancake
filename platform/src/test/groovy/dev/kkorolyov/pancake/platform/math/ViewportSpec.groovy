package dev.kkorolyov.pancake.platform.math

import spock.lang.Shared
import spock.lang.Specification

class ViewportSpec extends Specification {
	@Shared
	int xParts = 3
	@Shared
	int yParts = 3
	@Shared
	int zParts = 4

	Viewport viewport = new Viewport(xParts, yParts, zParts)

	def "calculates origin"() {
		when:
		viewport.set(13)

		then:
		viewport.getOrigin(1, 1, 1) == Vector3.of(0.3333333333333333, 0.3333333333333333, 0.250)
	}
	def "calculates size"() {
		int width = xParts * 4
		int height = yParts * 4
		int depth = zParts * 5

		expect:
		viewport.getSize(width, height, depth) == Vector3.of(width / xParts, height / yParts, depth / zParts)
	}
}
