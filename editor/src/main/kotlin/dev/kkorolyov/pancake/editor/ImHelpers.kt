package dev.kkorolyov.pancake.editor

import imgui.flag.ImGuiTableFlags

/**
 * Helper for drawing a 2-column table suited for displaying property `(name, value)` pairs.
 * Labels the table [id] and invokes [op] within the table.
 */
inline fun propertiesTable(id: String, op: Op) {
	table(id, 2, ImGuiTableFlags.SizingStretchProp, op)
}
/**
 * Helper for drawing a 2-column row suited for displaying property [label] with [op].
 */
inline fun propertyRow(label: String, op: Op) {
	column { text(label) }
	column(op)
}
