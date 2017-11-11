package dev.kkorolyov.pancake.skillet.ui.entity;

import dev.kkorolyov.pancake.skillet.model.GenericEntity;
import dev.kkorolyov.pancake.skillet.model.Model.ModelChangeEvent;
import dev.kkorolyov.pancake.skillet.model.ModelListener;
import dev.kkorolyov.pancake.skillet.model.Workspace;
import dev.kkorolyov.pancake.skillet.model.Workspace.WorkspaceChangeEvent;
import dev.kkorolyov.pancake.skillet.ui.Panel;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static dev.kkorolyov.pancake.skillet.decorator.UIDecorator.decorate;

/**
 * Displays editing entities as selectable tabs.
 */
public class EntityTabPane implements Panel, ModelListener<Workspace> {
	private final Map<GenericEntity, Tab> tabs = new HashMap<>();
	private final TabPane root = new TabPane();

	private Consumer<GenericEntity> entitySelected;
	private Consumer<GenericEntity> entityClosed;

	/**
	 * Constructs a new entity tab pane.
	 */
	public EntityTabPane() {
		decorate(root)
				.styleClass("entity-tabs");
	}

	/**
	 * Adds a new entity tab if it does not yet exist and selects it.
	 * @param entity associated entity
	 */
	public void add(GenericEntity entity) {
		Tab entityTab = tabs.computeIfAbsent(entity, k -> {
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
							})
					.close(() -> entityClosed(entity))
					.get();

			root.getTabs().add(tab);
			return tab;
		});
		root.getSelectionModel().select(entityTab);
	}

	private void entitySelected(GenericEntity entity) {
		if (entitySelected != null) entitySelected.accept(entity);
	}
	/** @param entitySelected listener invoked with the selected entity when an entity is selected */
	public EntityTabPane onEntitySelected(Consumer<GenericEntity> entitySelected) {
		this.entitySelected = entitySelected;
		return this;
	}

	private void entityClosed(GenericEntity entity) {
		if (entityClosed != null) entityClosed.accept(entity);
		tabs.remove(entity);
		if (tabs.isEmpty()) entitySelected(null);
	}
	/** @param entityClosed listener invoked with the closed entity when its editor is closed */
	public EntityTabPane onEntityClosed(Consumer<GenericEntity> entityClosed) {
		this.entityClosed = entityClosed;
		return this;
	}

	private void removeOldEntities(Workspace workspace) {
		tabs.entrySet().stream()
				.filter(entry -> !workspace.containsEntity(entry.getKey()))
				.collect(Collectors.toSet())
				.forEach(entry -> {
					tabs.remove(entry.getKey());
					root.getTabs().remove(entry.getValue());
				});
	}

	@Override
	public Node getRoot() {
		return root;
	}

	@Override
	public void changed(Workspace target, ModelChangeEvent event) {
		if (WorkspaceChangeEvent.ACTIVE == event) target.getActiveEntity().ifPresent(this::add);
		else if (WorkspaceChangeEvent.REMOVE == event) removeOldEntities(target);
	}
}
