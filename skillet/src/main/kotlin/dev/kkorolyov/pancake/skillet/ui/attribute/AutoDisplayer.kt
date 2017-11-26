package dev.kkorolyov.pancake.skillet.ui.attribute

import javafx.scene.Node
import kotlin.collections.MutableMap.MutableEntry

/**
 * Displays properties using the most appropriate displayer.
 */
object AutoDisplayer : Displayer<Any>(Any::class) {
	private val strategies: Collection<Displayer<Any>> = setOf(
			URIDisplayer as Displayer<Any>,
			NumberDisplayer as Displayer<Any>,
			VectorDisplayer as Displayer<Any>,
			StringDisplayer as Displayer<Any>,
			MapDisplayer as Displayer<Any>
	)

	override fun display(attribute: MutableEntry<String, Any>): Node =
			strategies.first { it.accepts(attribute) }
					.display(attribute)
}
