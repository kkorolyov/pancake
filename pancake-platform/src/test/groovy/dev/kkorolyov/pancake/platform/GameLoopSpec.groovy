package dev.kkorolyov.pancake.platform

import spock.lang.Specification

import static SpecUtilities.randLong

class GameLoopSpec extends Specification {
	GameEngine engine = Mock()

	GameLoop loop = new GameLoop(engine)

	def "invokes engine.update on handle"() {
		when:
		loop.handle(now)

		then:
		1 * engine.update(_)

		where:
		now << randLong()
	}
}
