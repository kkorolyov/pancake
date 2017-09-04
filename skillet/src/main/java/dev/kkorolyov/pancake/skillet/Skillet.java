package dev.kkorolyov.pancake.skillet;

import dev.kkorolyov.pancake.skillet.data.Attribute;
import dev.kkorolyov.pancake.skillet.data.Component;
import dev.kkorolyov.pancake.skillet.data.ComponentFactory;
import dev.kkorolyov.pancake.skillet.data.Entity;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.LinkedHashMap;
import java.util.Map;

import static dev.kkorolyov.pancake.skillet.utility.ui.UIBuilder.buildMenuItem;

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
						buildMenuItem("_Save", e -> save()),
						buildMenuItem("_Load", e -> load()),
						buildMenuItem("_Reload", e -> reload()))));

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

		entity = new Entity("Entity", componentFactory)
				.addComponent(new Component("Component1")
						.addAttribute(text)
						.addAttribute(num)
						.addAttribute(map))
				.addComponent(new Component("Component2"));
	}

	private void save() {

	}
	private void load() {

	}
	private void reload() {
		Node node = entity.toNode();

		pane.setCenter(node);
		BorderPane.setAlignment(node, Pos.TOP_CENTER);
	}
}
