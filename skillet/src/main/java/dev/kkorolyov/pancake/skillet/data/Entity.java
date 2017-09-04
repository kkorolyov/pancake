package dev.kkorolyov.pancake.skillet.data;

import dev.kkorolyov.pancake.skillet.display.Displayable;
import dev.kkorolyov.pancake.skillet.utility.ui.DisplayableTransformer;
import dev.kkorolyov.simpleprops.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import static dev.kkorolyov.pancake.skillet.utility.ui.UIBuilder.attachContextMenu;
import static dev.kkorolyov.pancake.skillet.utility.ui.UIBuilder.buildMenuItem;
import static dev.kkorolyov.pancake.skillet.utility.ui.UIBuilder.buildTitledPane;

/**
 * A container of components.
 */
public class Entity implements Displayable {
	private final String name;
	private final Map<String, Component> components = new LinkedHashMap<>();
	private final ComponentFactory componentFactory;
	private final ObservableList<String> unattachedComponents = FXCollections.observableArrayList();

	/**
	 * Constructs a new entity.
	 * @param name entity name
	 * @param componentFactory component factory providing fresh component instances
	 */
	public Entity(String name, ComponentFactory componentFactory) {
		this.name = name;
		this.componentFactory = componentFactory;
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

	/** @return	this entity as a properties instance */
	public Properties save() {
		// TODO
		return null;
	}

	/** @return entity name */
	public String getName() {
		return name;
	}

	private ObservableList<String> updateUnattachedComponents() {
		unattachedComponents.clear();
		StreamSupport.stream(componentFactory.getNames().spliterator(), false)
				.filter(name -> !containsComponent(name))
				.forEach(unattachedComponents::add);

		return unattachedComponents;
	}

	@Override
	public Node toNode() {
		VBox componentsBox = DisplayableTransformer.asPane(components.values(), VBox::new, this::wrapComponent);

		ScrollPane componentsPane = new ScrollPane(componentsBox);
		componentsPane.setFitToWidth(true);
		componentsPane.setFitToHeight(true);

		return attachContextMenu(buildTitledPane(name, "Right click to add component", componentsBox, false),
				() -> new ContextMenu(StreamSupport.stream(componentFactory.getNames().spliterator(), false)
						.filter(name -> !containsComponent(name))
						.map(name -> buildMenuItem("Add component: " + name, e1 -> {
							Component newComponent = componentFactory.get(name);

							addComponent(newComponent);
							componentsBox.getChildren().add(wrapComponent(newComponent, newComponent.toNode(), componentsBox));
						})).toArray(MenuItem[]::new)
				));
	}
	private Node wrapComponent(Component data, Node node, Pane nodeParent) {
		return attachContextMenu(node, () ->
				new ContextMenu(buildMenuItem("Remove component: " + data.getName(), e -> {
					removeComponent(data.getName());
					nodeParent.getChildren().remove(node);
				})));
	}
}
