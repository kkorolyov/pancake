package dev.kkorolyov.pancake.platform.storage.serialization

import spock.lang.Shared
import spock.lang.Specification

abstract class AbstractSerializerSpec<I, O, S extends Serializer<I, O>> extends Specification {
	@Shared List<I> inReps = []
	@Shared List<O> outReps = []

	S serializer

	def "reads"() {
		expect:
		serializer.read(outRep) == inRep

		where:
		inRep << inReps
		outRep << outReps
	}
	def "writes"() {
		expect:
		serializer.write(inRep) == outRep

		where:
		inRep << inReps
		outRep << outReps
	}
}
