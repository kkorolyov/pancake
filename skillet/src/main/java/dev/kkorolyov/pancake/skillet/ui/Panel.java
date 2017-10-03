package dev.kkorolyov.pancake.skillet.ui;

import javafx.scene.Node;

/**
 * A self-contained displayable element.
 */
public interface Panel {
	/** @return root node of panel */
	Node getRoot();
}