package dev.kkorolyov.pancake.skillet.ui.component;

import dev.kkorolyov.pancake.skillet.ui.Panel;
import dev.kkorolyov.pancake.skillet.ui.attribute.AttributePanel;
import dev.kkorolyov.pancake.storage.Attribute;
import dev.kkorolyov.pancake.storage.Component;
import dev.kkorolyov.pancake.storage.Component.ComponentChangeEvent;
import dev.kkorolyov.pancake.storage.Storable.StorableChangeEvent;
import dev.kkorolyov.pancake.storage.StorableListener;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import java.util.stream.Collectors;

import static dev.kkorolyov.pancake.skillet.UIDecorator.decorate;

/**
 * Displays a {@link Component}.
 */
public class ComponentPanel implements Panel, StorableListener<Component> {
	private final VBox content = new VBox();
	private final TitledPane root = new TitledPane();

	private Runnable componentRemoveAction;

	/**
	 * Constructs a new component display.
	 * @param component displayed component
	 */
	public ComponentPanel(Component component) {
		content.getChildren().addAll(component.getAttributes().stream()
				.map(attribute -> new AttributePanel(attribute).getRoot())
				.collect(Collectors.toList()));

		decorate(root)
				.id(component.getName())
				.styleClass("component")
				.contentDisplay(ContentDisplay.GRAPHIC_ONLY)
				.graphic(decorate(new BorderPane())
						.left(decorate(new Label(component.getName()))
								.styleClass("component-name")
								.get())
						.right(decorate(new Button())
								.contentDisplay(ContentDisplay.GRAPHIC_ONLY)
								.graphic(decorate(new Rectangle(8, 2))
										.styleClass("minus")
										.get())
								.styleClass("remove-component")
								.tooltip("Remove component")
								.action(this::componentRemoveAction)
								.get())
						.bind(Region::minWidthProperty, root.widthProperty().subtract(28))
						.get())
				.content(decorate(content)
						.styleClass("component-content")
						.get());

		component.register(this);
	}

	private void componentRemoveAction() {
		if (componentRemoveAction != null) componentRemoveAction.run();
	}
	/** @param componentRemoveAction listener invoked when this panel is requested for removal */
	public void onComponentRemoveAction(Runnable componentRemoveAction) {
		this.componentRemoveAction = componentRemoveAction;
	}

	@Override
	public TitledPane getRoot() {
		return root;
	}

	@Override
	public void changed(Component target, StorableChangeEvent event) {
		if (ComponentChangeEvent.ADD == event) {
			for (Attribute attribute : target.getAttributes()) {
				if (content.getChildren().stream().noneMatch(node -> node.getId().equals(attribute.getName()))) {
					content.getChildren().add(new AttributePanel(attribute).getRoot());
				}
			}
		} else if (ComponentChangeEvent.REMOVE == event) {
			content.getChildren().removeIf(next -> target.getAttributes().stream().noneMatch(attribute -> attribute.getName().equals(next.getId())));
		}
	}
}
