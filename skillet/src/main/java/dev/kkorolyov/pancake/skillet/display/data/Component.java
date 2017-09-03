package dev.kkorolyov.pancake.skillet.display.data;

import dev.kkorolyov.pancake.skillet.display.Displayable;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

import static dev.kkorolyov.pancake.skillet.uibuilder.DisplayableTransformer.asPane;

/**
 * A container of attributes.
 */
public class Component implements Displayable {
	private final String name;
	private final List<Attribute> attributes = new ArrayList<>();

	/**
	 * Constructs a new component.
	 * @param name component name
	 */
	public Component(String name) {
		this.name = name;
	}

	/** @return component name */
	public String getName() {
		return name;
	}

	/**
	 * @param attribute added attribute
	 * @return {@code this}
	 */
	public Component addAttribute(Attribute attribute) {
		attributes.add(attribute);
		return this;
	}

	@Override
	public Node toNode() {
		return new TitledPane(name, asPane(attributes, VBox.class));
	}
}
