package dev.kkorolyov.pancake.skillet.ui.component

import dev.kkorolyov.pancake.skillet.decorator.action
import dev.kkorolyov.pancake.skillet.decorator.bind
import dev.kkorolyov.pancake.skillet.decorator.content
import dev.kkorolyov.pancake.skillet.decorator.contentDisplay
import dev.kkorolyov.pancake.skillet.decorator.graphic
import dev.kkorolyov.pancake.skillet.decorator.left
import dev.kkorolyov.pancake.skillet.decorator.right
import dev.kkorolyov.pancake.skillet.decorator.styleClass
import dev.kkorolyov.pancake.skillet.decorator.tooltip
import dev.kkorolyov.pancake.skillet.model.GenericComponent
import dev.kkorolyov.pancake.skillet.model.GenericEntity
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
 * @param component displayed component
 * @param entity entity owning `component`
 */
class ComponentPanel(
		component: GenericComponent,
		entity: GenericEntity
) : Panel {
	companion object {
		private val autoDisplayer: Displayer<Any> = AutoDisplayer
	}
	private val content: VBox = VBox()
			.styleClass("component-content")
	override val root: TitledPane = TitledPane()
			.styleClass("component")

	init {
		for (display in component.attributes.entries.map(autoDisplayer::display)) content.children += display
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
								.action { entity -= component.name })
						.bind(Region::minWidthProperty, root.widthProperty().subtract(28)))
				.content(content)
	}
}
