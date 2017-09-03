package dev.kkorolyov.pancake.skillet.uibuilder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import java.util.Arrays;

/**
 * Convenience methods for building JavaFX Menus.
 */
public final class MenuBuilder {
	private MenuBuilder() {}

	public static MenuBar buildMenuBar(Menu... menus) {
		MenuBar menuBar = new MenuBar();
		Arrays.stream(menus).forEach(menu ->
				menuBar.getMenus().add(menu));

		return menuBar;
	}

	public static Menu buildMenu(String title, MenuItem... menuItems) {
		Menu menu = new Menu(title);
		Arrays.stream(menuItems).forEach(menuItem ->
				menu.getItems().add(menuItem));

		return menu;
	}

	public static MenuItem buildMenuItem(String title, EventHandler<ActionEvent> handler) {
		MenuItem menuItem = new MenuItem(title);
		menuItem.setOnAction(handler);

		return menuItem;
	}
}
