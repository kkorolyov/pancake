package dev.kkorolyov.pancake.graphics.io

import dev.kkorolyov.pancake.graphics.component.Model
import dev.kkorolyov.pancake.platform.entity.ComponentConverter

class ModelComponentConverter : ComponentConverter<Model> {
	override fun read(data: Any): Model {
		TODO("Not yet implemented")
	}

	override fun write(t: Model): Any {
		TODO("Not yet implemented")
	}

	override fun getType(): Class<Model> = Model::class.java
}
