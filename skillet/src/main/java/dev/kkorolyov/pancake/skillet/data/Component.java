package dev.kkorolyov.pancake.skillet.data;

import dev.kkorolyov.pancake.skillet.display.Displayable;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static dev.kkorolyov.pancake.skillet.utility.ui.DisplayableTransformer.asPane;

/**
 * A container of attributes.
 */
public class Component implements Displayable, Serializable {
	private static final long serialVersionUID = -5144869208259619358L;

	private final String name;
	private final List<Attribute> attributes = new ArrayList<>();

	/**
	 * Constructs a new component.
	 * @param name component name
	 */
	public Component(String name) {
		this.name = name;
	}


	/**
	 * @param attribute added attribute
	 * @return {@code this}
	 */
	public Component addAttribute(Attribute attribute) {
		attributes.add(attribute);
		return this;
	}

	/** @return component name */
	public String getName() {
		return name;
	}

	@Override
	public Node toNode() {
		return new TitledPane(name, asPane(attributes, VBox::new));
	}
}
