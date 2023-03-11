package dev.kkorolyov.pancake.core.system

import dev.kkorolyov.pancake.platform.entity.EntityPool

import spock.lang.Specification

class CappingSystemSpec extends Specification {
	EntityPool entities = new EntityPool()
	CappingSystem system = new CappingSystem()

	def "caps values above"() {
		// TODO
	}

	def "ignores values below"() {
		// TODO
	}
}
