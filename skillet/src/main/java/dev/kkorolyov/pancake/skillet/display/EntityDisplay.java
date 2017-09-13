package dev.kkorolyov.pancake.skillet.display;

import dev.kkorolyov.pancake.muffin.data.DataChangeListener;
import dev.kkorolyov.pancake.muffin.data.DataObservable.DataChangeEvent;
import dev.kkorolyov.pancake.muffin.data.type.Component;
import dev.kkorolyov.pancake.muffin.data.type.Entity;
import dev.kkorolyov.pancake.muffin.data.type.Entity.EntityChangeEvent;
import dev.kkorolyov.pancake.skillet.ComponentFactory;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.action;
import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.change;
import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.collapsible;
import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.tooltip;

/**
 * Displays an {@link Entity}.
 */
public class EntityDisplay implements Display, DataChangeListener<Entity> {
	private final TextField nameField;
	private final VBox content;
	private final TitledPane root;

	/**
	 * Constructs a new entity disityy.
	 * @param entity displayed entity
	 * @param componentFactory component factory providing all addable components
	 */
	public EntityDisplay(Entity entity, ComponentFactory componentFactory) {
		content = new VBox(entity.getComponents().stream()
				.map(component -> buildComponentDisplay(entity, component))
				.toArray(Node[]::new));

		nameField = change((target, oldValue, newValue) -> entity.setName(newValue),
				TextInputControl::textProperty,
				tooltip("Entity name",
						new TextField(entity.getName())));

		MenuButton addButton = change((target, oldValue, newValue) -> {
					target.getItems().clear();
					componentFactory.getNames().stream()
							.filter(name -> !entity.containsComponent(name))
							.map(name -> action(
									e1 -> entity.addComponent(componentFactory.get(name)),
									new MenuItem(name)))
							.forEach(target.getItems()::add);
				},
				MenuButton::showingProperty,
				tooltip("Add component",
						new MenuButton("+")));
		addButton.getStyleClass().add("add-component");

		Pane header = buildHeader(nameField, addButton);

		ScrollPane scrollPane = new ScrollPane(content);
		scrollPane.setFitToWidth(true);

		root = collapsible(false,
				new TitledPane(entity.getName(), scrollPane));
		root.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		root.setGraphic(header);
		root.setId(entity.getName());

		header.minWidthProperty().bind(root.widthProperty().subtract(12));

		entity.register(this);
	}

	@Override
	public TitledPane getRoot() {
		return root;
	}

	@Override
	public void changed(Entity target, DataChangeEvent event) {
		if (EntityChangeEvent.NAME == event) {
			nameField.setText(target.getName());
		} else if (EntityChangeEvent.ADD == event) {
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

		Button removeButton = action(e -> entity.removeComponent(component.getName()),
				tooltip("Remove component",
						new Button("-")));
		removeButton.getStyleClass().add("remove-component");

		Pane header = buildHeader(label, removeButton);

		TitledPane componentRoot = new ComponentDisplay(component).getRoot();
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
