package dev.kkorolyov.pancake.skillet.ui.component;

import dev.kkorolyov.pancake.skillet.ComponentFactory;
import dev.kkorolyov.pancake.skillet.ComponentFactory.ComponentFactoryChangeEvent;
import dev.kkorolyov.pancake.skillet.model.GenericComponent;
import dev.kkorolyov.pancake.skillet.model.GenericEntity;
import dev.kkorolyov.pancake.skillet.model.Storable.StorableChangeEvent;
import dev.kkorolyov.pancake.skillet.model.StorableListener;
import dev.kkorolyov.pancake.skillet.ui.Panel;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import static dev.kkorolyov.pancake.skillet.decorator.UIDecorator.decorate;

/**
 * Displays a list of components which may be added to the designed entity.
 */
public class ComponentList implements Panel, StorableListener<ComponentFactory> {
	private final Map<String, Button> componentButtons = new HashMap<>();
	private final VBox content = new VBox();
	private final VBox root = new VBox(
			decorate(new Label("Components"))
					.styleClass("panel-header")
					.get(),
			decorate(new ScrollPane(content))
					.compact()
					.get());

	private GenericEntity lastKnown;
	private Consumer<GenericComponent> componentSelected;

	/**
	 * Constructs a new component list.
	 * @param componentFactory factory providing addable components
	 */
	public ComponentList(ComponentFactory componentFactory) {
		componentFactory.register(this);
	}

	/**
	 * Disables buttons of all components present in {@code entity}.
	 * Enables buttons of all others.
	 */
	public void refreshComponents(GenericEntity entity) {
		lastKnown = entity;

		componentButtons.forEach((name, button) -> button.setDisable(lastKnown == null || lastKnown.containsComponent(name)));
	}

	private void componentSelected(GenericComponent component) {
		if (componentSelected != null) componentSelected.accept(component);
	}
	/** @param componentSelected listener invoked with the selected component when a component is selected */
	public void onComponentSelected(Consumer<GenericComponent> componentSelected) {
		this.componentSelected = componentSelected;
	}

	@Override
	public Node getRoot() {
		return root;
	}

	@Override
	public void changed(ComponentFactory target, StorableChangeEvent event) {
		if (ComponentFactoryChangeEvent.ADD == event) {
			target.getNames()
					.forEach(name ->
							componentButtons.computeIfAbsent(name, k -> {
								Button button = decorate(new Button(k))
										.maxSize(Double.MAX_VALUE, null)
										.action(() -> componentSelected(target.get(name)))
										.get();

								content.getChildren().add(button);
								return button;
							}));
		} else if (ComponentFactoryChangeEvent.REMOVE == event) {
			Iterator<Entry<String, Button>> it = componentButtons.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Button> next = it.next();
				if (!target.contains(next.getKey())) {
					it.remove();
					content.getChildren().remove(next.getValue());
				}
			}
		}
		refreshComponents(lastKnown);
	}
}
