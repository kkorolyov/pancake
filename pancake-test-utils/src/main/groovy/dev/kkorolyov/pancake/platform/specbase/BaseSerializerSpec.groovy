package dev.kkorolyov.pancake.platform.specbase

import dev.kkorolyov.pancake.platform.serialization.AutoSerializer
import dev.kkorolyov.pancake.platform.serialization.Serializer

import spock.lang.Specification
/**
 * Base specification for {@link Serializer} implementations.
 * Verifies {@code read} and {@code write} between representations mapped to each other.
 */
abstract class BaseSerializerSpec<I, O, S extends Serializer<I, O>> extends Specification {
	static Set<String> ignore = []

	Map<I, O> reps = [:]

	S serializer

	def "reads"() {
		if ('reads' in ignore) return

		expect:
		reps.every {k, v ->
			serializer.read(v) == k
		}
	}
	def "writes"() {
		if ('writes' in ignore) return

		expect:
		reps.every {k,v ->
			serializer.write(k) == v
		}
	}

	protected AutoSerializer mockAutoSerializer(List<? super I> inList, List<O> outList) {
		return Mock(AutoSerializer) {
			read({it in outList}) >> {inList[outList.indexOf(it[0])]}
			write({it in inList}) >> {outList[inList.indexOf(it[0])]}
		}
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
