package dev.kkorolyov.pancake.skillet.ui.entity;

import dev.kkorolyov.pancake.skillet.model.GenericEntity;
import dev.kkorolyov.pancake.skillet.model.GenericEntity.EntityChangeEvent;
import dev.kkorolyov.pancake.skillet.model.Model.ModelChangeEvent;
import dev.kkorolyov.pancake.skillet.model.ModelListener;
import dev.kkorolyov.pancake.skillet.model.Workspace;
import dev.kkorolyov.pancake.skillet.model.Workspace.WorkspaceChangeEvent;
import dev.kkorolyov.pancake.skillet.ui.Panel;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.kkorolyov.pancake.skillet.decorator.UIDecorator.decorate;

public class EntityList implements Panel, ModelListener<Workspace> {
	private final Map<GenericEntity, Button> buttons = new HashMap<>();
	private final VBox content = new VBox();
	private final VBox root = new VBox(
			decorate(new Label("Entities"))
					.styleClass("panel-header")
					.get(),
			decorate(new ScrollPane(content))
					.compact()
					.get());

	/**
	 * Constructs a new entity list.
	 * @param workspace workspace maintaining entities
	 */
	public EntityList(Workspace workspace) {
		workspace.register(this);
	}

	private void addNewEntities(Workspace workspace) {
		workspace.getEntities().forEach(entity ->
				buttons.computeIfAbsent(entity, k -> {
					Button button = decorate(new Button(k.getName()))
							.maxSize(Double.MAX_VALUE, null)
							.action(() -> workspace.setActiveEntity(k))
							.contextMenu(() -> decorate(new ContextMenu())
									.item("Delete", () -> workspace.removeEntity(k))
									.get())
							.get();

					entity.register((target, event) -> {
						if (EntityChangeEvent.NAME == event) button.setText(target.getName());
					});
					content.getChildren().add(button);
					return button;
				})
		);
	}
	private void removeOldEntities(Workspace workspace) {
		buttons.entrySet().stream()
				.filter(entry -> !workspace.containsEntity(entry.getKey()))
				.collect(Collectors.toSet())
				.forEach(entry -> {
					buttons.remove(entry.getKey());
					content.getChildren().remove(entry.getValue());
				});
	}

	@Override
	public Node getRoot() {
		return root;
	}

	@Override
	public void changed(Workspace target, ModelChangeEvent event) {
		if (WorkspaceChangeEvent.ADD == event) addNewEntities(target);
		else if (WorkspaceChangeEvent.REMOVE == event) removeOldEntities(target);
	}
}
