package dev.kkorolyov.pancake.skillet;

import dev.kkorolyov.pancake.platform.storage.Component;
import dev.kkorolyov.pancake.platform.storage.Entity;
import dev.kkorolyov.pancake.skillet.ui.component.ComponentList;
import dev.kkorolyov.pancake.skillet.ui.entity.EntityTabPane;

import javafx.application.Application;
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
import java.util.List;
import java.util.stream.Collectors;

import static dev.kkorolyov.pancake.skillet.UIDecorator.decorate;

public class Skillet extends Application {
	private static final String ENTITY_FILE_EXTENSION = ".mfn";

	private Stage stage;

	private final ComponentFactory componentFactory = new ComponentFactory();
	private Entity entity;

	private final EntityTabPane entityTabPane = new EntityTabPane();
	private final ComponentList componentList = new ComponentList(componentFactory);

	private final ResourceHandler resourceHandler = new ResourceHandler();

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;

		entityTabPane.onEntitySelected(selected -> {
			entity = selected;
			componentList.refreshComponents(entity);
		});

		componentList.onComponentSelected(component -> {
			entity.addComponent(component);
			componentList.refreshComponents(entity);
		});

		addDefaultComponents();

		primaryStage.setTitle("Skillet - Pancake Entity Designer");
		primaryStage.getIcons().add(new Image("pancake-icon.png"));

		BorderPane root = new BorderPane();

		// Menu bar
		root.setTop(new MenuBar(
				new Menu("_Entity", null,
						decorate(new MenuItem("_New"))
								.action(this::newEntity)
								.get(),
						decorate(new MenuItem("_Save"))
								.action(this::saveEntity)
								.get(),
						decorate(new MenuItem("_Open"))
								.action(this::loadEntity)
								.get()),
				new Menu("_Components", null,
						decorate(new MenuItem("_Load"))
								.action(this::loadComponents)
								.get())));

		Scene scene = new Scene(root, 480, 480);
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
					case L:
						loadComponents();
						break;
				}
			}
		});

		root.setCenter(entityTabPane.getRoot());
		root.setRight(componentList.getRoot());

		primaryStage.setScene(scene);
		primaryStage.show();
	}
	private void addDefaultComponents() {
		try {
			componentFactory.add(
					resourceHandler.load(Paths.get(ClassLoader.getSystemResource("defaults" + ENTITY_FILE_EXTENSION).toURI())),
					false);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private void newEntity() {
		entityTabPane.add(new Entity());
	}
	private void saveEntity() {
		if (entity == null) return;

		FileChooser chooser = buildFileChooser();
		chooser.setInitialFileName(entity.getName() + ENTITY_FILE_EXTENSION);

		File result = chooser.showSaveDialog(stage);
		if (result != null) {
			resourceHandler.save(entity, result.toPath());
		}
	}
	private void loadEntity() {
		FileChooser chooser = buildFileChooser();
		chooser.setTitle("Open Entity");

		File result = chooser.showOpenDialog(stage);
		if (result != null) {
			Iterable<Component> components = resourceHandler.load(result.toPath());

			componentFactory.add(components, false);

			entityTabPane.add(new Entity(result.getName().replaceAll(ENTITY_FILE_EXTENSION, ""), components));
		}
	}

	private void loadComponents() {
		FileChooser chooser = buildFileChooser();
		chooser.setTitle("Load Components");

		List<File> results = chooser.showOpenMultipleDialog(stage);
		if (results != null) {
			List<Component> addedComponents = new ArrayList<>();

			for (File file : results) {
				resourceHandler.load(file.toPath()).forEach(addedComponents::add);
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
