package dev.kkorolyov.pancake.skillet.display.data;

import dev.kkorolyov.pancake.skillet.display.Displayable;
import dev.kkorolyov.pancake.skillet.uibuilder.DisplayableTransformer;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import java.util.LinkedHashMap;
import java.util.Map;

import static dev.kkorolyov.pancake.skillet.uibuilder.ButtonBuilder.buildButton;

/**
 * A container of components.
 */
public class Entity implements Displayable {
	private final String name;
	private final Map<String, Component> components = new LinkedHashMap<>();

	/**
	 * Constructs a new entity.
	 * @param name entity name
	 */
	public Entity(String name) {
		this.name = name;
	}

	/** @return entity name */
	public String getName() {
		return name;
	}

	/**
	 * @param name name to search by
	 * @return {@code true} if this entity contains a component of name {@code name}
	 */
	public boolean containsComponent(String name) {
		return components.containsKey(name);
	}

	/**
	 * @param component added component
	 * @return {@code this}
	 */
	public Entity addComponent(Component component) {
		components.put(component.getName(), component);
		return this;
	}
	/**
	 * @param name removed component name
	 * @return removed component, or {@code null} if no such component
	 */
	public Component removeComponent(String name) {
		return components.remove(name);
	}

	@Override
	public Node toNode() {
		VBox componentsBox = DisplayableTransformer.asPane(components.values(), VBox.class);

		return new VBox(buildButton(
				name,
				"Click to collapse components",
				e -> componentsBox.lookupAll("TitledPane")
						.forEach(node -> ((TitledPane) node).setExpanded(false))),
				componentsBox);
	}
}
