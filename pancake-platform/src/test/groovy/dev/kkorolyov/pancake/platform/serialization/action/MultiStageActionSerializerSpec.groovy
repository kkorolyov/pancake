package dev.kkorolyov.pancake.platform.serialization.action

import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.action.MultiStageAction
import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec

import java.util.stream.Collectors

import static dev.kkorolyov.pancake.platform.SpecUtilities.*

class MultiStageActionSerializerSpec extends BaseSerializerSpec<MultiStageAction, String> {
	float holdThreshold = randFloat()
	List<Action> actions = (1..3).collect {Mock(Action)}
	List<String> actionsS = actions.collect {randString()}

	def setup() {
		reps << [(new MultiStageAction(actions[0], actions[1], actions[2], holdThreshold)): actionsS.stream().collect(Collectors.joining(", ", "{", "}"))]

		serializer = new MultiStageActionSerializer(Mock(ActionRegistry))
		mockAutoSerializer(actions, actionsS)
		setField("holdThreshold", serializer, holdThreshold)
	}
}
