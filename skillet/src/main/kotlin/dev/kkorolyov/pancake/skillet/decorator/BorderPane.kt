package dev.kkorolyov.pancake.skillet.decorator

import javafx.scene.Node
import javafx.scene.layout.BorderPane

fun <T : BorderPane> T.top(node: Node): T {
	top = node
	return this
}
fun <T : BorderPane> T.bottom(node: Node): T {
	bottom = node
	return this
}
fun <T : BorderPane> T.left(node: Node): T {
	left = node
	return this
}
fun <T : BorderPane> T.right(node: Node): T {
	right = node
	return this
}
fun <T : BorderPane> T.center(node: Node): T {
	center = node
	return this
}
