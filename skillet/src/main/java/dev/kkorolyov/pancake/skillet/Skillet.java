package dev.kkorolyov.pancake.skillet;

import dev.kkorolyov.pancake.skillet.data.Component;
import dev.kkorolyov.pancake.skillet.data.ComponentFactory;
import dev.kkorolyov.pancake.skillet.data.Entity;
import dev.kkorolyov.pancake.skillet.display.EntityDisplay;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
		addDefaultComponents();

		primaryStage.setTitle("Skillet - Pancake Entity Designer");
		primaryStage.getIcons().add(new Image("pancake-icon.png"));

		// Menu bar
		pane.setTop(new MenuBar(
				new Menu("_Entity", null,
						action(e -> newEntity(),
								new MenuItem("_New")),
						action(e -> saveEntity(),
								new MenuItem("_Save")),
						action(e -> loadEntity(),
								new MenuItem("_Load")),
						action(e -> reloadEntity(),
								new MenuItem("_Reload"))),
				new Menu("_Components", null,
						action(e -> loadComponents(primaryStage),
								new MenuItem("_Load")))));

		Scene scene = new Scene(pane, 480, 480);
		scene.getStylesheets().add("style.css");

		primaryStage.setScene(scene);
		primaryStage.show();
	}
	private void addDefaultComponents() {
		try {
			componentFactory.add(Paths.get(ClassLoader.getSystemResource("defaults.pan").toURI()));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private void newEntity() {
		entity = new Entity();

		reloadEntity();
	}
	private void saveEntity() {

	}
	private void loadEntity() {

	}
	private void reloadEntity() {
		Node node = new EntityDisplay(entity, componentFactory).getRoot();

		pane.setCenter(node);
		BorderPane.setAlignment(node, Pos.TOP_CENTER);
	}

	private void loadComponents(Window window) {
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().add(new ExtensionFilter("Pancake Entities", "*.pan"));

		List<Component> addedComponents = new ArrayList<>();
		for (File file : chooser.showOpenMultipleDialog(window)) {
			addedComponents.addAll(componentFactory.add(file.toPath()));
		}
		Alert addedComponentsInfo = new Alert(
				AlertType.INFORMATION,
				addedComponents.stream()
						.map(component -> component.getName() + " - " + component.getAttributes().size() + " attributes")
						.collect(Collectors.joining(System.lineSeparator())));
		addedComponentsInfo.setTitle("Added Components");
		addedComponentsInfo.setHeaderText(addedComponentsInfo.getTitle());

		addedComponentsInfo.show();
	}
}
