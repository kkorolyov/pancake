package dev.kkorolyov.pancake.skillet.display;

import dev.kkorolyov.pancake.skillet.data.Attribute;
import dev.kkorolyov.pancake.skillet.data.Component;
import dev.kkorolyov.pancake.skillet.data.Component.ComponentChangeEvent;
import dev.kkorolyov.pancake.skillet.data.DataChangeListener;
import dev.kkorolyov.pancake.skillet.data.DataObservable.DataChangeEvent;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import java.util.stream.Collectors;

import static dev.kkorolyov.pancake.skillet.utility.ui.DisplayTransformer.asPane;

/**
 * Displays a {@link Component}.
 */
public class ComponentDisplay implements Display, DataChangeListener<Component> {
	private final VBox content;
	private final TitledPane root;

	/**
	 * Constructs a new component display.
	 * @param component displayed component
	 */
	public ComponentDisplay(Component component) {
		content = asPane(component.getAttributes().stream()
				.map(AttributeDisplay::new)
				.collect(Collectors.toList()), VBox::new);

		root = new TitledPane(component.getName(), content);
		root.setId(component.getName());

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
					content.getChildren().add(new AttributeDisplay(attribute).getRoot());
				}
			}
		} else if (ComponentChangeEvent.REMOVE == event) {
			content.getChildren().removeIf(next -> target.getAttributes().stream().noneMatch(attribute -> attribute.getName().equals(next.getId())));
		}
	}
}
