package dev.kkorolyov.pancake.platform.media

import dev.kkorolyov.pancake.platform.math.Vector

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector

class CameraSpec extends Specification {
	@Shared Vector position = new Vector()
	@Shared Vector unitPixels = new Vector()
	@Shared float width = 0
	@Shared float height = 0

	Camera camera = new Camera(position, unitPixels, width, height)

	def "gets set position"() {
		Vector vector = randVector()

		when:
		camera.setPosition(vector)

		then:
		camera.getPosition() == vector
	}
	def "gets constructor-set position"() {
		expect:
		camera.getPosition() == position
	}

	def "gets set unitPixels"() {
		Vector vector = randVector()

		when:
		camera.setUnitPixels(vector)

		then:
		camera.getUnitPixels() == vector
	}
	def "gets constructor-set unitPixels"() {
		expect:
		camera.getUnitPixels() == unitPixels
	}
}
