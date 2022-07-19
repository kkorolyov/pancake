package dev.kkorolyov.pancake.editor.data

import dev.kkorolyov.pancake.editor.ComponentData
import dev.kkorolyov.pancake.platform.entity.Component

data class BasicComponentData(private val component: Component) :
	ComponentData<Component, BasicComponentData>(component) {
}
