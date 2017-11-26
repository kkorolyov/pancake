package dev.kkorolyov.pancake.skillet.serialization

import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec
import dev.kkorolyov.pancake.skillet.model.GenericEntity
import dev.kkorolyov.pancake.skillet.model.Workspace

class WorkspaceSerializerSpec extends BaseSerializerSpec {
	Iterable<GenericEntity> entities = []
	String entitiesS = ""

	def setup() {
		Workspace workspace = new Workspace()
		entities.forEach {workspace.&add}
		reps << [(workspace): entitiesS]

		serializer = new WorkspaceSerializer()
	}
}
