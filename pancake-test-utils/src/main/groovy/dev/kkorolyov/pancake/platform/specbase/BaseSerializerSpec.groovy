package dev.kkorolyov.pancake.platform.specbase

import dev.kkorolyov.pancake.platform.serialization.AutoSerializer
import dev.kkorolyov.pancake.platform.serialization.Serializer

import spock.lang.Specification

import static dev.kkorolyov.simplespecs.SpecUtilities.setField

/**
 * Base specification for {@link Serializer} implementations.
 * Verifies {@code read} and {@code write} between representations mapped to each other.
 */
abstract class BaseSerializerSpec<I, O> extends Specification {
	Map<I, O> reps = [:]

	Serializer<I, O> serializer

	def "reads"() {
		expect:
		reps.each { k, v ->
			assert serializer.read(v) == k
		}
	}
	def "writes"() {
		expect:
		reps.each { k, v ->
			assert serializer.read(serializer.write(k)) == k
		}
	}

	protected <T> void mockAutoSerializer(Map<T, O> inOut) {
		setField("autoSerializer", serializer, Mock(AutoSerializer) {
			inOut.each { i, o ->
				it.read(o) >> i
				it.write(i) >> o
			}
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
		return reps.findResult(null) { k, v ->
			if (v == outRep) return k
		}
	}
}
