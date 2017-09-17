package dev.kkorolyov.pancake.skillet.ui.panel;

import dev.kkorolyov.pancake.muffin.data.type.Entity;
import dev.kkorolyov.pancake.skillet.ui.Panel;
import dev.kkorolyov.pancake.skillet.ui.type.EntityPanel;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.decorate;

/**
 * Displays all in-design entities.
 */
public class EntityTabs implements Panel {
	private final Map<Entity, Tab> entities = new HashMap<>();
	private final TabPane root = new TabPane();

	private Consumer<Entity> entitySelected;
	private Consumer<Entity> entityRemoved;

	/**
	 * Adds a new entity tab if it does not yet exist.
	 * @param entity associated entity
	 */
	public void add(Entity entity) {
		entities.computeIfAbsent(entity, k -> {
			Tab tab = decorate(new Tab())
					.change((target, oldValue, newValue) -> entityRemoved(entity),
							Tab::onClosedProperty)
					.get();
			tab.setGraphic(new EntityTab(entity).getRoot());
			tab.setContent(new EntityPanel(entity).getRoot());

			root.getTabs().add(tab);
			return tab;
		});
	}

	private void entitySelected(Entity entity) {
		if (entitySelected != null) entitySelected.accept(entity);
	}
	/** @param entitySelected listener invoked with the selected entity when an entity tab is selected */
	public void setOnEntitySelected(Consumer<Entity> entitySelected) {
		this.entitySelected = entitySelected;
	}

	private void entityRemoved(Entity entity) {
		if (entityRemoved != null) entityRemoved.accept(entity);
	}
	/** @param entityRemoved listener invoked with the removed entity when an entity tab is removed */
	public void setOnEntityRemoved(Consumer<Entity> entityRemoved) {
		this.entityRemoved = entityRemoved;
	}

	@Override
	public Node getRoot() {
		return root;
	}
}
