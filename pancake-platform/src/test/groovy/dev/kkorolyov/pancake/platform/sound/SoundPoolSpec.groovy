package dev.kkorolyov.pancake.platform.sound

import spock.lang.Shared
import spock.lang.Specification

class SoundPoolSpec extends Specification {
	@Shared int cacheSize = 20

	SoundPool soundPool = new SoundPool(cacheSize)

	def "excepts on retrieving by unassociated name"() {
		when:
		soundPool.get("notAName")

		then:
		thrown(NoSuchElementException)
	}
}
