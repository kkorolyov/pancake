package dev.kkorolyov.pancake.skillet.display;

import javafx.scene.Node;

/**
 * Data which can be displayed as a JavaFX node.
 */
public interface Displayable {
	/** @return visual representation of this data */
	Node toNode();
}
