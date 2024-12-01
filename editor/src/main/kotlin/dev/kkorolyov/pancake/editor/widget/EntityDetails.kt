package dev.kkorolyov.pancake.editor.widget

import dev.kkorolyov.pancake.editor.DebouncedValue
import dev.kkorolyov.pancake.editor.Layout
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.contextMenu
import dev.kkorolyov.pancake.editor.data.OwnedComponent
import dev.kkorolyov.pancake.editor.factory.getWidget
import dev.kkorolyov.pancake.editor.getValue
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.onDrag
import dev.kkorolyov.pancake.editor.selectable
import dev.kkorolyov.pancake.editor.toStructEntity
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.entity.Entity
import imgui.ImGui
import imgui.flag.ImGuiPopupFlags
import io.github.classgraph.ClassGraph
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import kotlin.math.max

private val componentTypes by ThreadLocal.withInitial {
	ClassGraph().enableClassInfo().scan().use { result ->
		result.getClassesImplementing(Component::class.java)
			.filter { !it.isAbstract && !it.isInterface }
			.map { it.loadClass(Component::class.java) }
	}
}

/**
 * Displays and provides for modification of [entity] properties.
 * If [dragDropId] is provided, emits drag-drop payloads to it containing the selected [OwnedComponent].
 */
class EntityDetails(private val entity: Entity, private val dragDropId: String? = null) : Widget {
	private val currentDetails = DebouncedValue<Component, Widget> { getWidget(Component::class.java, it) }
	private val currentDragPayload = DebouncedValue<Component, OwnedComponent> { OwnedComponent(entity, it) }

	private val inlineDetails = Popup("inlineDetails")

	private var create: Modal? = null
	private var createContent: Widget? = null

	private var toAdd: Component? = null
	private var toRemove: Class<out Component>? = null

	override fun invoke() {
		list("##components", width = Layout.stretchWidth, height = max(Layout.lineHeight(entity.size() + 1.2), Layout.free.y - Layout.lineHeight(1.5))) {
			entity.forEach {
				selectable(it.debugName) {
					inlineDetails.open(currentDetails.set(it))
				}
				contextMenu {
					menuItem("remove") {
						toRemove = it::class.java
					}
				}

				dragDropId?.let { id ->
					onDrag {
						setDragDropPayload(currentDragPayload.set(it), id)
						currentDetails.set(it)()
					}
				}
			}

			selectable("##empty") {}
			contextMenu(flags = ImGuiPopupFlags.MouseButtonLeft) {
				componentTypes.forEach { type ->
					if (entity[type] == null) {
						menuItem(type.simpleName) {
							create = Modal("New ${type.simpleName}")
							createContent = getWidget(Component::class.java, type) {
								create?.close()
								toAdd = it
							}
						}
					}
				}
			}
			tooltip("add component")
			inlineDetails()
		}
		button("copy yaml") {
			ImGui.setClipboardText(Yaml(DumperOptions().apply { width = 1000 }).dump(toStructEntity(entity)))
		}

		// augment elements only after done iterating
		toAdd?.let {
			entity.put(it)
			toAdd = null
		}
		toRemove?.let {
			entity.remove(it)
			toRemove = null
		}

		// open outside the menu creating the modal because ID stack
		createContent?.let {
			create?.open(it)
			createContent = null
		}
		create?.invoke()
	}
}
