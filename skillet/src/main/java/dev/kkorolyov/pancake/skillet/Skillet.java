package dev.kkorolyov.pancake.skillet;

import dev.kkorolyov.pancake.skillet.data.Attribute;
import dev.kkorolyov.pancake.skillet.data.Component;
import dev.kkorolyov.pancake.skillet.data.ComponentFactory;
import dev.kkorolyov.pancake.skillet.data.Entity;
import dev.kkorolyov.pancake.skillet.display.EntityDisplay;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.LinkedHashMap;
import java.util.Map;

import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.action;

public class Skillet extends Application {
	private final BorderPane pane = new BorderPane();
	private final ComponentFactory componentFactory = new ComponentFactory();
	private Entity entity;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Skillet - Pancake Entity Designer");

		// Menu bar
		pane.setTop(new MenuBar(
				new Menu("_File", null,
						action(e -> save(),
								new MenuItem("_Save")),
						action(e -> load(),
								new MenuItem("_Load")),
						action(e -> reload(),
								new MenuItem("_Reload")))));

		primaryStage.setScene(new Scene(pane, 480, 480));
		primaryStage.show();

		// TODO Testing shit below
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("1", "234234");
		m.put("sdfs43wy5eyh", "YOOOOO");
		Map<String, Object> m2 = new LinkedHashMap<>();
		m2.put("2", m);

		Attribute text = new Attribute("TextAttr", "Stringo");
		Attribute num = new Attribute("NumAttr", 4);
		Attribute map = new Attribute("MapAttr", m2);

		entity = new Entity("Entity");
		componentFactory
				.add(new Component("Component1")
						.addAttribute(text)
						.addAttribute(num)
						.addAttribute(map))
				.add(new Component("Component2"));
	}

	private void save() {

	}
	private void load() {

	}
	private void reload() {
		Node node = new EntityDisplay(entity, componentFactory).getRoot();

		pane.setCenter(node);
		BorderPane.setAlignment(node, Pos.TOP_CENTER);
	}
}
