package dev.kkorolyov.pancake.skillet.ui.component;

import dev.kkorolyov.pancake.skillet.model.GenericComponent;
import dev.kkorolyov.pancake.skillet.ui.Panel;
import dev.kkorolyov.pancake.skillet.ui.attribute.AutoDisplayer;
import dev.kkorolyov.pancake.skillet.ui.attribute.Displayer;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import java.util.stream.Collectors;

import static dev.kkorolyov.pancake.skillet.decorator.UIDecorator.decorate;

/**
 * Displays a {@link GenericComponent}.
 */
public class ComponentPanel implements Panel {
	private static Displayer<Object> autoDisplayer = new AutoDisplayer();

	private final VBox content = new VBox();
	private final TitledPane root = new TitledPane();

	private Runnable componentRemoveAction;

	/**
	 * Constructs a new component display.
	 * @param component displayed component
	 */
	public ComponentPanel(GenericComponent component) {
		content.getChildren().addAll(component.stream()
				.map(entry -> decorate(autoDisplayer.display(entry))
						.id(entry.getKey())
						.get())
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
}
