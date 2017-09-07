package dev.kkorolyov.pancake.skillet.display;

import dev.kkorolyov.pancake.skillet.data.Component;
import dev.kkorolyov.pancake.skillet.data.ComponentFactory;
import dev.kkorolyov.pancake.skillet.data.DataChangeListener;
import dev.kkorolyov.pancake.skillet.data.DataObservable.DataChangeEvent;
import dev.kkorolyov.pancake.skillet.data.Entity;
import dev.kkorolyov.pancake.skillet.data.Entity.EntityChangeEvent;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.action;
import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.collapsible;
import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.contextMenu;
import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.tooltip;

/**
 * Displays an {@link Entity}.
 */
public class EntityDisplay implements Display, DataChangeListener<Entity> {
	private final VBox content;
	private final TitledPane root;

	/**
	 * Constructs a new entity display.
	 * @param entity displayed entity
	 * @param componentFactory component factory providing all addable components
	 */
	public EntityDisplay(Entity entity, ComponentFactory componentFactory) {
		content = new VBox(entity.getComponents().stream()
				.map(component -> buildComponentDisplay(entity, component))
				.toArray(Node[]::new));

		root = contextMenu(() ->
						new ContextMenu(componentFactory.getNames().stream()
								.filter(name -> !entity.containsComponent(name))
								.map(name -> action(e ->
												entity.addComponent(componentFactory.get(name)),
										new MenuItem("Add component: " + name)))
								.toArray(MenuItem[]::new)),
				collapsible(false,
						tooltip("Right click to add component",
								new TitledPane(entity.getName(), content))));
		root.setId(entity.getName());

		entity.register(this);
	}

	@Override
	public TitledPane getRoot() {
		return root;
	}

	@Override
	public void changed(Entity target, DataChangeEvent event) {
		if (EntityChangeEvent.ADD == event) {
			for (Component component : target.getComponents()) {
				if (content.getChildren().stream().noneMatch(node -> node.getId().equals(component.getName()))) {
					content.getChildren().add(buildComponentDisplay(target, component));
				}
			}
		} else if (EntityChangeEvent.REMOVE == event) {
			content.getChildren().removeIf(next -> target.getComponents().stream().noneMatch(component -> component.getName().equals(next.getId())));
		}
	}

	private Node buildComponentDisplay(Entity entity, Component component) {
		return contextMenu(() ->
						new ContextMenu(action(e -> {
							entity.removeComponent(component.getName());
						}, new MenuItem("Remove component: " + component.getName()))),
				tooltip("Right click to remove component",
						new ComponentDisplay(component).getRoot()));
	}
}
