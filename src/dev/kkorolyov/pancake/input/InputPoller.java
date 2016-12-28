package dev.kkorolyov.pancake.input;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import dev.kkorolyov.pancake.entity.Action;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simplelogs.Logger.Level;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

/**
 * Polls pressed keys and applies them as appropriate actions.
 */
public class InputPoller {
	private static final Logger log = Logger.getLogger(InputPoller.class.getName(), Level.DEBUG, new PrintWriter(System.err));	// TODO Temp
	
	private Map<KeyCode, Action> keyMap;
	private Map<MouseButton, Action> mouseKeyMap;
	private final Set<KeyCode> pressedKeys = new CopyOnWriteArraySet<>();
	private final Set<MouseButton> pressedMouseKeys = new CopyOnWriteArraySet<>();
	
	private final Set<Action> actions = new HashSet<>();

	/**
	 * Constructs a new input poller listening to a scene and generating actions according to key and mouse maps.
	 * @param scene scene emitting {@code KeyEvents}
	 * @param keyMap mapping of keyboard keys to actions
	 * @param mouseKeyMap mapping of mouse keys to actions
	 */
	public InputPoller(Scene scene, Map<KeyCode, Action> keyMap, Map<MouseButton, Action> mouseKeyMap) {
		this.keyMap = keyMap;
		this.mouseKeyMap = mouseKeyMap;
		
		registerSceneListeners(scene);
	}
	private void registerSceneListeners(Scene scene) {
		scene.setOnKeyPressed((key) -> {
			KeyCode code = key.getCode();
			if (keyMap.containsKey(code)) {	// Skip unmapped keys
				pressedKeys.add(key.getCode());
				log.debug("Key pressed: " + code);
			}
		});
		scene.setOnKeyReleased((key) -> {
			KeyCode code = key.getCode();
			pressedKeys.remove(code);
			log.debug("Key released: " + code);
		});
		scene.setOnMousePressed((mouseKey) -> {
			MouseButton code = mouseKey.getButton();
			if (mouseKeyMap.containsKey(code)) {
				pressedMouseKeys.add(code);
				log.debug("Key pressed: MOUSE " + code);
			}
		});
		scene.setOnMouseReleased((mouseKey) -> {
			MouseButton code = mouseKey.getButton();
			pressedMouseKeys.remove(code);
			log.debug("Key released: MOUSE " + code);
		});
	}
	
	/**
	 * Polls all pressed keys and creates a set of their respective actions.
	 * @return all unique actions polled during this method invocation
	 */
	public Set<Action> poll() {
		actions.clear();
		
		for (KeyCode key : pressedKeys)
			actions.add(keyMap.get(key));
		for (MouseButton key : pressedMouseKeys)
			actions.add(mouseKeyMap.get(key));
		
		return actions;
	}
}
