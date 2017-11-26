package dev.kkorolyov.pancake.skillet.ui.component

import dev.kkorolyov.pancake.skillet.model.GenericComponent
import dev.kkorolyov.pancake.skillet.model.GenericEntity
import dev.kkorolyov.pancake.skillet.tooltip
import dev.kkorolyov.pancake.skillet.ui.Panel
import dev.kkorolyov.pancake.skillet.ui.attribute.AutoDisplayer
import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay.GRAPHIC_ONLY
import javafx.scene.control.Label
import javafx.scene.control.TitledPane
import javafx.scene.layout.BorderPane
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
	private val content: VBox = VBox().apply {
		styleClass += "component-content"
	}
	override val root: TitledPane = TitledPane().apply {
		styleClass += "component"

		contentDisplay = GRAPHIC_ONLY
		graphic = BorderPane().apply {
			left = Label(component.name).apply {
				styleClass += "component-name"
			}
			right = Button().apply {
				styleClass += "remove-component"
				contentDisplay = GRAPHIC_ONLY
				graphic = Rectangle(8.0, 2.0).apply {
					styleClass += "minus"
				}
				tooltip("Remove component")
				setOnAction { entity -= component.name }
			}
			minWidthProperty().bind(widthProperty().subtract(28))
		}
		content = this@ComponentPanel.content
	}

	init {
		for (display in component.attributes.entries.map(AutoDisplayer::display)) content.children += display
	}
}
