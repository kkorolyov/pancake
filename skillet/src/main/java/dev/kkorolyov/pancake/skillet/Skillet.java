package dev.kkorolyov.pancake.skillet;

import dev.kkorolyov.pancake.muffin.data.type.Component;
import dev.kkorolyov.pancake.muffin.data.type.Entity;
import dev.kkorolyov.pancake.muffin.persistence.ComponentPersister;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static dev.kkorolyov.pancake.skillet.utility.ui.UIDecorator.action;

public class Skillet extends Application {
	private static final String ENTITY_FILE_EXTENSION = ".mfn";

	private Stage stage;
	private final BorderPane pane = new BorderPane();

	private final ComponentPersister componentPersister = new ComponentPersister();
	private final ComponentFactory componentFactory = new ComponentFactory();
	private Entity entity;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;

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
								new MenuItem("_Open")),
						action(e -> reloadEntity(),
								new MenuItem("_Reload"))),
				new Menu("_Components", null,
						action(e -> loadComponents(),
								new MenuItem("_Load")))));

		Scene scene = new Scene(pane, 480, 480);
		scene.getStylesheets().add("style.css");

		// Shortcuts
		scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.isControlDown()) {
				switch (e.getCode()) {
					case N:
						newEntity();
						break;
					case S:
						saveEntity();
						break;
					case O:
						loadEntity();
						break;
					case R:
						reloadEntity();
						break;
					case L:
						loadComponents();
						break;
				}
			}
		});
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	private void addDefaultComponents() {
		try {
			componentFactory.add(
					componentPersister.loadComponents(Paths.get(ClassLoader.getSystemResource("defaults" + ENTITY_FILE_EXTENSION).toURI())),
					false);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private void newEntity() {
		entity = new Entity();

		reloadEntity();
	}
	private void saveEntity() {
		if (entity == null) return;

		FileChooser chooser = buildFileChooser();
		chooser.setInitialFileName(entity.getName() + ENTITY_FILE_EXTENSION);

		File result = chooser.showSaveDialog(stage);
		if (result != null) {
			componentPersister.saveComponents(entity.getComponents(), result.toPath());
		}
	}
	private void loadEntity() {
		FileChooser chooser = buildFileChooser();
		chooser.setTitle("Open Entity");

		File result = chooser.showOpenDialog(stage);
		if (result != null) {
			Collection<Component> components = componentPersister.loadComponents(result.toPath());

			componentFactory.add(components, false);

			entity = new Entity(result.getName().replaceAll(ENTITY_FILE_EXTENSION, ""), components);
		}
		reloadEntity();
	}
	private void reloadEntity() {
		if (entity == null) return;

		Node node = new EntityDisplay(entity, componentFactory).getRoot();

		pane.setCenter(node);
		BorderPane.setAlignment(node, Pos.TOP_CENTER);
	}

	private void loadComponents() {
		FileChooser chooser = buildFileChooser();
		chooser.setTitle("Load Components");

		List<File> results = chooser.showOpenMultipleDialog(stage);
		if (results != null) {
			List<Component> addedComponents = new ArrayList<>();

			for (File file : results) {
				addedComponents.addAll(componentPersister.loadComponents(file.toPath()));
			}
			componentFactory.add(addedComponents, false);

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

	private FileChooser buildFileChooser() {
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().add(new ExtensionFilter("Pancake Entities", "*" + ENTITY_FILE_EXTENSION));
		chooser.setInitialDirectory(new File("").getAbsoluteFile());

		return chooser;
	}
}
