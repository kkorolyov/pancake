package dev.kkorolyov.pancake.skillet;

import dev.kkorolyov.pancake.skillet.display.data.Attribute;
import dev.kkorolyov.pancake.skillet.display.data.Component;
import dev.kkorolyov.pancake.skillet.display.data.Entity;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.LinkedHashMap;
import java.util.Map;

import static dev.kkorolyov.pancake.skillet.uibuilder.MenuBuilder.buildMenu;
import static dev.kkorolyov.pancake.skillet.uibuilder.MenuBuilder.buildMenuBar;
import static dev.kkorolyov.pancake.skillet.uibuilder.MenuBuilder.buildMenuItem;

public class Skillet extends Application {
	private Entity entity;
	private BorderPane pane = new BorderPane();

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Skillet - Pancake Entity Designer");

		// Menu bar
		pane.setTop(buildMenuBar(
				buildMenu("_File",
						buildMenuItem("_Save", e -> save()),
						buildMenuItem("_Load", e -> load()),
						buildMenuItem("_Reload", e -> reload()))));

		primaryStage.setScene(new Scene(pane, 480, 480));
		primaryStage.show();

		// TODO Testing shit below
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("1", 234234);
		m.put("sdfs43wy5eyh", "YOOOOO");

		Attribute text = new Attribute("TextAttr", "Stringo");
		Attribute num = new Attribute("NumAttr", 4);
		Attribute map = new Attribute("MapAttr", m);

		entity = new Entity("Entity")
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
		pane.setCenter(entity.toNode());
	}
}
