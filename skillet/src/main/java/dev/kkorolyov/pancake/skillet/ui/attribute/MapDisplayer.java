package dev.kkorolyov.pancake.skillet.ui.attribute;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import java.util.Map;
import java.util.Map.Entry;

import static dev.kkorolyov.pancake.skillet.decorator.UIDecorator.decorate;

/**
 * Displays maps.
 */
public class MapDisplayer extends Displayer<Map<String, Object>> {
	private static final Displayer<Object> autoDisplayer = new AutoDisplayer();

	/**
	 * Constructs a new map displayer.
	 */
	public MapDisplayer() {
		super(Map.class);
	}

	@Override
	public Node display(Entry<String, Map<String, Object>> entry) {
		return decorate(new TitledPane())
				.styleClass("attribute")
				.graphic(decorate(new Label(entry.getKey()))
						.styleClass("attribute-name")
						.tooltip(getTooltipText())
						.get())
				.content(decorate(new VBox(entry.getValue().entrySet().stream()
						.map(autoDisplayer::display)
						.toArray(Node[]::new)))
						.styleClass("attribute-content")
						.get())
				.get();
	}
}
