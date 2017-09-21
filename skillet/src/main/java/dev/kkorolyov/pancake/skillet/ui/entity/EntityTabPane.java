package dev.kkorolyov.pancake.skillet.ui.entity;

import dev.kkorolyov.pancake.skillet.ui.Panel;
import dev.kkorolyov.pancake.storage.Entity;
import dev.kkorolyov.pancake.storage.Entity.EntityChangeEvent;
import dev.kkorolyov.pancake.storage.Storable.StorableChangeEvent;
import dev.kkorolyov.pancake.storage.StorableListener;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static dev.kkorolyov.pancake.skillet.UIDecorator.decorate;

/**
 * Displays editing entities as selectable tabs.
 */
public class EntityTabPane implements Panel, StorableListener<Entity> {
	private final Map<Entity, Tab> entities = new HashMap<>();
	private final TabPane root = new TabPane();

	private Consumer<Entity> entitySelected;

	/**
	 * Constructs a new entity tab pane.
	 */
	public EntityTabPane() {
		decorate(root)
				.styleClass("entity-tabs");
	}

	/**
	 * Adds a new entity tab if it does not yet exist.
	 * @param entity associated entity
	 */
	public void add(Entity entity) {
		entities.computeIfAbsent(entity, k -> {
			Tab tab = decorate(new Tab())
					.id(entity.getName())
					.styleClass("entity-tab")
					.graphic(decorate(new EntityLabel(entity).getRoot())
							.styleClass("entity-name")
							.get())
					.content(decorate(new EntityPanel(entity).getRoot())
							.styleClass("entity-content")
							.get())
					.change(Tab::selectedProperty,
							(target, oldValue, newValue) -> {
								if (newValue) entitySelected(entity);
								else entityUnselected(entity);
							})
					.close(() -> entityClosed(entity))
					.get();

			root.getTabs().add(tab);
			return tab;
		});
	}

	private void entitySelected(Entity entity) {
		if (entity != null) entity.register(this);

		if (entitySelected != null) entitySelected.accept(entity);
	}
	/** @param entitySelected listener invoked with the selected entity when an entity is selected */
	public void onEntitySelected(Consumer<Entity> entitySelected) {
		this.entitySelected = entitySelected;
	}

	private void entityUnselected(Entity entity) {
		entity.unregister(this);
	}

	private void entityClosed(Entity entity) {
		entities.remove(entity);
		if (entities.isEmpty()) entitySelected(null);
	}

	@Override
	public Node getRoot() {
		return root;
	}

	@Override
	public void changed(Entity target, StorableChangeEvent event) {
		if (EntityChangeEvent.REMOVE == event) entitySelected(target);
	}
}
