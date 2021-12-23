package dev.kkorolyov.pancake.input.jfx.component

import dev.kkorolyov.pancake.input.jfx.Reaction
import dev.kkorolyov.pancake.platform.entity.Component
import javafx.scene.input.InputEvent

/**
 * An entity controller which reacts to JavaFX input events.
 */
class Input(private val delegate: Reaction<InputEvent>) : Component, Reaction<InputEvent> by delegate
