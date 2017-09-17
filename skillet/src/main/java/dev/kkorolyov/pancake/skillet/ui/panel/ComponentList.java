package dev.kkorolyov.pancake.skillet.ui.panel;

import dev.kkorolyov.pancake.muffin.data.DataChangeListener;
import dev.kkorolyov.pancake.muffin.data.DataObservable.DataChangeEvent;
import dev.kkorolyov.pancake.muffin.data.type.Component;
import dev.kkorolyov.pancake.muffin.data.type.Entity;
import dev.kkorolyov.pancake.skillet.ComponentFactory;
import dev.kkorolyov.pancake.skillet.ComponentFactory.ComponentFactoryChangeEvent;
import dev.kkorolyov.pancake.skillet.ui.Panel;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.decorate;
import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.scroll;

/**
 * Displays a list of components which may be added to the designed entity.
 */
public class ComponentList implements Panel, DataChangeListener<ComponentFactory> {
	private final Map<String, Button> componentButtons = new HashMap<>();
	private final VBox content = new VBox();
	private final VBox root = new VBox(
			decorate(new Label("Components"))
					.styleClass("panel-header")
					.get(),
			scroll(content));

	private Consumer<Component> componentSelected;

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
	public void disableComponents(Entity entity) {
		componentButtons.forEach((key, value) -> value.setDisable(entity.containsComponent(key)));
	}

	private void componentSelected(Component component) {
		if (componentSelected != null) componentSelected.accept(component);
	}
	/** @param componentSelected listener invoked with the selected component when a component is selected */
	public void onComponentSelected(Consumer<Component> componentSelected) {
		this.componentSelected = componentSelected;
	}

	@Override
	public Node getRoot() {
		return root;
	}

	@Override
	public void changed(ComponentFactory target, DataChangeEvent event) {
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
	}
}
