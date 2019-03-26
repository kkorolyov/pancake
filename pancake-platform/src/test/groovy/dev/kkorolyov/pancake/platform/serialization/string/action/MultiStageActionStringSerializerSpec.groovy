package dev.kkorolyov.pancake.platform.serialization.string.action

import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.action.MultiStageAction
import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec

import java.util.stream.Collectors

import static dev.kkorolyov.simplespecs.SpecUtilities.randFloat
import static dev.kkorolyov.simplespecs.SpecUtilities.randString
import static dev.kkorolyov.simplespecs.SpecUtilities.setField

class MultiStageActionStringSerializerSpec extends BaseSerializerSpec<MultiStageAction, String> {
	float holdThreshold = randFloat()
	Map<Action, String> inOut = (1..3).collectEntries {
		[(Mock(Action)): randString()]
	}

	def setup() {
		reps << [(new MultiStageAction(inOut.keySet()[0], inOut.keySet()[1], inOut.keySet()[2], holdThreshold)): inOut.values().stream().collect(Collectors.joining(", ", "{", "}"))]

		serializer = new MultiStageActionStringSerializer(Mock(ActionRegistry))
		mockAutoSerializer(inOut)
		setField("holdThreshold", serializer, holdThreshold)
	}
}
