package dev.kkorolyov.pancake.skillet.ui.component;

import dev.kkorolyov.pancake.muffin.data.DataChangeListener;
import dev.kkorolyov.pancake.muffin.data.DataObservable.DataChangeEvent;
import dev.kkorolyov.pancake.muffin.data.type.Attribute;
import dev.kkorolyov.pancake.muffin.data.type.Component;
import dev.kkorolyov.pancake.muffin.data.type.Component.ComponentChangeEvent;
import dev.kkorolyov.pancake.skillet.ui.Panel;
import dev.kkorolyov.pancake.skillet.ui.attribute.AttributePanel;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import static dev.kkorolyov.pancake.skillet.utility.decorator.UIDecorator.decorate;

/**
 * Displays a {@link Component}.
 */
public class ComponentPanel implements Panel, DataChangeListener<Component> {
	private final VBox content;
	private final TitledPane root;

	/**
	 * Constructs a new component display.
	 * @param component displayed component
	 */
	public ComponentPanel(Component component) {
		content = new VBox(
				component.getAttributes().stream()
						.map(attribute -> new AttributePanel(attribute).getRoot())
						.toArray(Node[]::new));

		root = decorate(new TitledPane(component.getName(), content))
				.id(component.getName())
				.styleClass("component")
				.get();

		component.register(this);
	}

	@Override
	public TitledPane getRoot() {
		return root;
	}

	@Override
	public void changed(Component target, DataChangeEvent event) {
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
