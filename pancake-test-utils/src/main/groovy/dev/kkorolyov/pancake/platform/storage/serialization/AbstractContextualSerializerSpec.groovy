package dev.kkorolyov.pancake.platform.storage.serialization

abstract class AbstractContextualSerializerSpec<I, O, C> extends AbstractSerializerSpec<I, O, ContextualSerializer> {
	C context

	def "reads"() {
		expect:
		serializer.read(outRep, context) == inRep

		where:
		inRep << inReps
		outRep << outReps
	}
	def "writes"() {
		expect:
		serializer.write(inRep, context) == outRep

		where:
		inRep << inReps
		outRep << outReps
	}
}
