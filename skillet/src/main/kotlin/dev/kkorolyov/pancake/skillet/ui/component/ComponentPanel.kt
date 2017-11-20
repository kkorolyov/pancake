package dev.kkorolyov.pancake.skillet.ui.component

import dev.kkorolyov.pancake.skillet.decorator.*
import dev.kkorolyov.pancake.skillet.model.GenericComponent
import dev.kkorolyov.pancake.skillet.ui.Panel
import dev.kkorolyov.pancake.skillet.ui.attribute.AutoDisplayer
import dev.kkorolyov.pancake.skillet.ui.attribute.Displayer
import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay.GRAPHIC_ONLY
import javafx.scene.control.Label
import javafx.scene.control.TitledPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle

/**
 * Displays a [GenericComponent].
 * @param componentRemoved listener invoked with the associated component when it is removed
 */
class ComponentPanel(
		component: GenericComponent,
		componentRemoved: (GenericComponent) -> Unit = {}
) : Panel {
	companion object {
		private val autoDisplayer: Displayer<Any> = AutoDisplayer()
	}
	private val content: VBox = VBox()
			.styleClass("component-content")
	override val root: TitledPane = TitledPane()
			.styleClass("component")

	init {
		component.map(autoDisplayer::display)
				.forEach { content.children += it }
		root
				.contentDisplay(GRAPHIC_ONLY)
				.graphic(BorderPane()
						.left(Label(component.name)
								.styleClass("component-name"))
						.right(Button()
								.styleClass("remove-component")
								.contentDisplay(GRAPHIC_ONLY)
								.graphic(Rectangle(8.0, 2.0)
										.styleClass("minus"))
								.tooltip("Remove component")
								.action { componentRemoved(component) })
						.bind(Region::minWidthProperty, root.widthProperty().subtract(28)))
				.content(content)
	}
}
