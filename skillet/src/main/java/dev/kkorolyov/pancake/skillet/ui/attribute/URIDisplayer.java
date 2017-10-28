package dev.kkorolyov.pancake.skillet.ui.attribute;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import java.net.URI;
import java.util.Map.Entry;

/**
 * Displays attributes with a URI value.
 */
public class URIDisplayer extends Displayer<URI> {
	/**
	 * Constructs a new URI displayer.
	 */
	public URIDisplayer() {
		super(URI.class);
	}

	@Override
	public Node display(Entry<String, URI> entry) {
		return simpleDisplay(entry.getKey(),
				// TODO
				new TextField(entry.getValue().toString()));
	}
}
