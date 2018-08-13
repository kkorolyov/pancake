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
	List<Action> actions = (1..3).collect {Mock(Action)}
	List<String> actionsS = actions.collect {randString()}

	def setup() {
		reps << [(new MultiStageAction(actions[0], actions[1], actions[2], holdThreshold)): actionsS.stream().collect(Collectors.joining(", ", "{", "}"))]

		serializer = new MultiStageActionStringSerializer(Mock(ActionRegistry))
		mockAutoSerializer(actions, actionsS)
		setField("holdThreshold", serializer, holdThreshold)
	}
}
