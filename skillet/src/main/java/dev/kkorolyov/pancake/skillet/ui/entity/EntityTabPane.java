package dev.kkorolyov.pancake.skillet.ui.entity;

import dev.kkorolyov.pancake.muffin.data.DataChangeListener;
import dev.kkorolyov.pancake.muffin.data.DataObservable.DataChangeEvent;
import dev.kkorolyov.pancake.muffin.data.type.Entity;
import dev.kkorolyov.pancake.muffin.data.type.Entity.EntityChangeEvent;
import dev.kkorolyov.pancake.skillet.ui.Panel;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static dev.kkorolyov.pancake.skillet.utility.decorator.UIDecorator.decorate;

/**
 * Displays editing entities as selectable tabs.
 */
public class EntityTabPane implements Panel, DataChangeListener<Entity> {
	private final Map<Entity, Tab> entities = new HashMap<>();
	private final TabPane root = new TabPane();

	private Entity selected;
	private Consumer<Entity> entitySelected;

	/**
	 * Adds a new entity tab if it does not yet exist.
	 * @param entity associated entity
	 */
	public void add(Entity entity) {
		entities.computeIfAbsent(entity, k -> {
			Tab tab = decorate(new Tab())
					.graphic(new EntityLabel(entity).getRoot())
					.content(new EntityPanel(entity).getRoot())
					.change(Tab::selectedProperty,
							(target, oldValue, newValue) -> {
								if (newValue) entitySelected(entity);
							})
					.get();

			root.getTabs().add(tab);
			return tab;
		});
		entity.register(this);
	}

	private void entitySelected(Entity entity) {
		selected = entity;

		if (entitySelected != null) entitySelected.accept(selected);
	}
	/** @param entitySelected listener invoked with the selected entity when an entity is selected */
	public void onEntitySelected(Consumer<Entity> entitySelected) {
		this.entitySelected = entitySelected;
	}

	@Override
	public Node getRoot() {
		return root;
	}

	@Override
	public void changed(Entity target, DataChangeEvent event) {
		if (EntityChangeEvent.REMOVE == event) entitySelected(target);
	}
}
