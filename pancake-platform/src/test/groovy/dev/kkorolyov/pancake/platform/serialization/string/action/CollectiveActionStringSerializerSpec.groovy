package dev.kkorolyov.pancake.platform.serialization.string.action

import dev.kkorolyov.pancake.platform.Registry
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.CollectiveAction
import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec

import java.util.stream.Collectors

import static dev.kkorolyov.simplespecs.SpecUtilities.randString

class CollectiveActionStringSerializerSpec extends BaseSerializerSpec<CollectiveAction, String> {
	Map<Action, String> inOut = (1..5).collectEntries {
		[(Mock(Action)): randString()]
	}

	def setup() {
		reps << [(new CollectiveAction(inOut.keySet())): inOut.values().stream().collect(Collectors.joining(", ", "[", "]"))]

		serializer = new CollectiveActionStringSerializer(Mock(Registry))
		mockAutoSerializer(inOut)
	}
}
