package dev.kkorolyov.pancake.skillet;

import dev.kkorolyov.pancake.skillet.model.GenericComponent;
import dev.kkorolyov.pancake.skillet.model.GenericEntity;
import dev.kkorolyov.pancake.skillet.model.Workspace;
import dev.kkorolyov.pancake.skillet.ui.component.ComponentList;
import dev.kkorolyov.pancake.skillet.ui.entity.EntityList;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static dev.kkorolyov.pancake.skillet.decorator.UIDecorator.decorate;

public class Skillet extends Application {
	private static final String ENTITY_FILE_EXTENSION = ".mfn";

	private final Workspace workspace = new Workspace();

	private Stage stage;

	private final EntityList entityList = new EntityList(workspace);
	private final ComponentList componentList = new ComponentList(workspace.getComponentFactory());
	private final EntityTabPane entityTabPane = new EntityTabPane();

	private final ResourceHandler resourceHandler = new ResourceHandler();

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;

		workspace.register(entityTabPane);

		entityTabPane
				.onEntitySelected(selected -> {
					workspace.setActiveEntity(selected);
					componentList.setEntity(selected);
				});

		componentList.onComponentSelected(component ->
				workspace.getActiveEntity()
						.ifPresent(entity -> entity.addComponent(component))
		);

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

		root.setLeft(entityList.getRoot());
		root.setRight(componentList.getRoot());
		root.setCenter(entityTabPane.getRoot());

		primaryStage.setScene(scene);
		primaryStage.show();
	}
	private void addDefaultComponents() {
		workspace.getComponentFactory().add(
				resourceHandler.load("defaults" + ENTITY_FILE_EXTENSION),
				false);
	}

	private void newEntity() {
		workspace.newEntity();
	}
	private void saveEntity() {
		workspace.getActiveEntity().ifPresent(entity -> {
			FileChooser chooser = buildFileChooser();
			chooser.setInitialFileName(entity.getName() + ENTITY_FILE_EXTENSION);

			File result = chooser.showSaveDialog(stage);
			if (result != null) {
				resourceHandler.save(entity, result.toString());
			}
		});
	}
	private void loadEntity() {
		FileChooser chooser = buildFileChooser();
		chooser.setTitle("Open Entity");

		File result = chooser.showOpenDialog(stage);
		if (result != null) {
			Iterable<GenericComponent> components = resourceHandler.load(result.toString());

			workspace.addEntity(new GenericEntity(result.getName().replaceAll(ENTITY_FILE_EXTENSION, ""), components));
		}
	}

	private void loadComponents() {
		FileChooser chooser = buildFileChooser();
		chooser.setTitle("Load Components");

		List<File> results = chooser.showOpenMultipleDialog(stage);
		if (results != null) {
			List<GenericComponent> addedComponents = new ArrayList<>();

			for (File file : results) {
				resourceHandler.load(file.toString()).forEach(addedComponents::add);
			}
			workspace.getComponentFactory().add(addedComponents, false);

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
