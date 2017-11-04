package dev.kkorolyov.pancake.platform.specbase

import dev.kkorolyov.pancake.platform.serialization.AutoSerializer
import dev.kkorolyov.pancake.platform.serialization.Serializer

import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.setField

/**
 * Base specification for {@link Serializer} implementations.
 * Verifies {@code read} and {@code write} between representations mapped to each other.
 */
abstract class BaseSerializerSpec<I, O> extends Specification {
	Map<I, O> reps = [:]

	Serializer<I, O> serializer

	def "reads"() {
		expect:
		reps.each {k, v ->
			assert serializer.read(v) == k
		}
	}
	def "writes"() {
		expect:
		reps.each {k,v ->
			assert serializer.read(serializer.write(k)) == k
		}
	}

	protected <T> void mockAutoSerializer(List<T> inList, List<O> outList) {
		setField("autoSerializer", serializer, Mock(AutoSerializer) {
			read({it in outList}) >> {inList[outList.indexOf(it[0])]}
			write({it in inList}) >> {outList[inList.indexOf(it[0])]}
		})
	}

	protected boolean hasIn(I inRep) {
		return reps.containsKey(inRep)
	}
	protected boolean hasOut(O outRep) {
		return reps.containsValue(outRep)
	}

	protected O outRep(I inRep) {
		return reps[(inRep)]
	}
	protected I inRep(O outRep) {
		return reps.findResult(null) {k, v ->
			if (v == outRep) return k
		}
	}
}
