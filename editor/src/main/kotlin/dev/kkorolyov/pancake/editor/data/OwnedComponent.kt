package dev.kkorolyov.pancake.editor.data

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity

/**
 * Unique combination of [entity] and its owned [component].
 */
data class OwnedComponent(val entity: Entity, val component: Component)
