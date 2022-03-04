package dev.kkorolyov.pancake.input.common.component

import dev.kkorolyov.pancake.input.common.Reaction
import dev.kkorolyov.pancake.platform.entity.Component

/**
 * Provides actions according to [delegate].
 */
class Input(private val delegate: Reaction<Any>) : Component, Reaction<Any> by delegate
