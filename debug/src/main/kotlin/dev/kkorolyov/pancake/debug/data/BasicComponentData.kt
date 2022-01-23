package dev.kkorolyov.pancake.debug.data

import dev.kkorolyov.pancake.debug.ComponentData
import dev.kkorolyov.pancake.platform.entity.Component

data class BasicComponentData(private val component: Component) :
	ComponentData<Component, BasicComponentData>(component) {
}
