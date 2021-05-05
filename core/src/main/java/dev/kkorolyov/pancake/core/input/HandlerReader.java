package dev.kkorolyov.pancake.core.input;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.MultiStageAction;
import dev.kkorolyov.pancake.platform.plugin.Application;
import dev.kkorolyov.pancake.platform.plugin.Plugins;
import dev.kkorolyov.pancake.platform.registry.Registry;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.toSet;

/**
 * Reads {@link Handler}s from configuration files.
 */
public final class HandlerReader {
	private final Application application;
	private final Registry<? super String, ? extends Action> actions;

	/**
	 * Constructs a new handler reader.
	 * @param actions actions to bind read handlers to
	 */
	public HandlerReader(Registry<? super String, ? extends Action> actions) {
		this(Plugins.application(), actions);
	}
	HandlerReader(Application application, Registry<? super String, ? extends Action> actions) {
		this.application = application;
		this.actions = actions;
	}

	/**
	 * Reads handlers from a {@code yaml} configuration and binds to actions in this reader's action registry.
	 * @param in file to read from
	 * @return handlers read from {@code in} and bound to this reader's actions
	 */
	public Collection<Handler> fromYaml(InputStream in) {
		Map<String, Collection<String>> resources = new Yaml().load(in);

		return resources.entrySet().stream()
				.map(e -> {
					Action action = actions.get(e.getKey());

					return new Handler(
							action instanceof MultiStageAction
									? (MultiStageAction) action
									: new MultiStageAction(action, null, null, 0),  // hold irrelevant
							e.getValue().stream()
									.map(application::toInput)
									.collect(toSet())
					);
				})
				.collect(toSet());
	}
}
