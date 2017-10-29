package dev.kkorolyov.pancake.platform.specbase

import dev.kkorolyov.pancake.platform.serialization.AutoContextualSerializer
import dev.kkorolyov.pancake.platform.serialization.ContextualSerializer
/**
 * Base specification for {@link ContextualSerializer} implementations.
 * Verifies contextual {@code read} and {@code write}.
 */
abstract class BaseContextualSerializerSpec<I, O, C> extends BaseSerializerSpec<I, O, ContextualSerializer> {
	C context

	def setupSpec() {
		ignore += ['reads', 'writes']
	}

	def "reads contextually"() {
		expect:
		reps.every {k, v ->
			serializer.read(v, context) == k
		}
	}
	def "writes contextually"() {
		expect:
		reps.every {k, v ->
			serializer.write(k, context) == v
		}
	}

	protected <T> AutoContextualSerializer mockAutoContextualSerializer(List<T> inList, List<O> outList) {
		return Mock(AutoContextualSerializer) {
			read({it in outList}, context) >> {inList[outList.indexOf(it[0])]}
			write({it in inList}, context) >> {outList[inList.indexOf(it[0])]}
		}
	}
}
