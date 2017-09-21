package dev.kkorolyov.pancake.skillet.ui.entity;

import dev.kkorolyov.pancake.skillet.ui.Panel;
import dev.kkorolyov.pancake.skillet.ui.component.ComponentPanel;
import dev.kkorolyov.pancake.storage.Component;
import dev.kkorolyov.pancake.storage.Entity;
import dev.kkorolyov.pancake.storage.Entity.EntityChangeEvent;
import dev.kkorolyov.pancake.storage.Storable.StorableChangeEvent;
import dev.kkorolyov.pancake.storage.StorableListener;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import static dev.kkorolyov.pancake.skillet.UIDecorator.decorate;

/**
 * Displays an {@link Entity}.
 */
public class EntityPanel implements Panel, StorableListener<Entity> {
	private final VBox content;
	private final ScrollPane root;

	/**
	 * Constructs a new entity display.
	 * @param entity displayed entity
	 */
	public EntityPanel(Entity entity) {
		content = decorate(new VBox(entity.getComponents().stream()
				.map(component -> buildComponentDisplay(entity, component))
				.toArray(Node[]::new)))
				.styleClass("entity-content")
				.get();

		root = decorate(new ScrollPane(content))
				.compact()
				.get();

		entity.register(this);
	}

	@Override
	public ScrollPane getRoot() {
		return root;
	}

	@Override
	public void changed(Entity target, StorableChangeEvent event) {
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
		ComponentPanel componentPanel = new ComponentPanel(component);
		componentPanel.onComponentRemoveAction(() -> entity.removeComponent(component.getName()));

		return componentPanel.getRoot();
	}
}
