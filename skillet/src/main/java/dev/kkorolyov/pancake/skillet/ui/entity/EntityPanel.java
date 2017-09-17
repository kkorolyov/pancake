package dev.kkorolyov.pancake.skillet.ui.entity;

import dev.kkorolyov.pancake.muffin.data.DataChangeListener;
import dev.kkorolyov.pancake.muffin.data.DataObservable.DataChangeEvent;
import dev.kkorolyov.pancake.muffin.data.type.Component;
import dev.kkorolyov.pancake.muffin.data.type.Entity;
import dev.kkorolyov.pancake.muffin.data.type.Entity.EntityChangeEvent;
import dev.kkorolyov.pancake.skillet.ui.Panel;
import dev.kkorolyov.pancake.skillet.ui.component.ComponentPanel;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import static dev.kkorolyov.pancake.skillet.utility.decorator.UIDecorator.decorate;

/**
 * Displays an {@link Entity}.
 */
public class EntityPanel implements Panel, DataChangeListener<Entity> {
	private final VBox content;
	private final ScrollPane root;

	/**
	 * Constructs a new entity display.
	 * @param entity displayed entity
	 */
	public EntityPanel(Entity entity) {
		content = new VBox(entity.getComponents().stream()
				.map(component -> buildComponentDisplay(entity, component))
				.toArray(Node[]::new));

		root = decorate(new ScrollPane(content))
				.id(entity.getName())
				.styleClass("entity")
				.compact()
				.get();

		entity.register(this);
	}

	@Override
	public ScrollPane getRoot() {
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
		Label label = new Label(component.getName());

		Button removeButton = decorate(new Button("-"))
				.styleClass("remove-component")
				.tooltip("Remove component")
				.action(() -> entity.removeComponent(component.getName()))
				.get();

		Pane header = buildHeader(label, removeButton);

		TitledPane componentRoot = new ComponentPanel(component).getRoot();
		componentRoot.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		componentRoot.setGraphic(header);

		header.minWidthProperty().bind(componentRoot.widthProperty().subtract(28));

		return componentRoot;
	}

	private Pane buildHeader(Node name, Node action) {
		BorderPane header = new BorderPane();
		header.setLeft(name);
		header.setRight(action);
		BorderPane.setAlignment(name, Pos.CENTER);
		BorderPane.setAlignment(header, Pos.CENTER);

		return header;
	}
}
