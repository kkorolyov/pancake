package dev.kkorolyov.pancake.core.registry;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.registry.Registry;
import dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory;
import dev.kkorolyov.simplefuncs.convert.Converter;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * {@link ResourceReaderFactory.ActionResource} for core actions.
 */
public class ActionResourceReaderFactory implements ResourceReaderFactory.ActionResource {
	private static final Pattern FORCE_PATTERN = Pattern.compile("(?i)FORCE(?-i)\\(\\)");

	@Override
	public Converter<String, Optional<? extends Action>> get(Registry<? super String, ? extends Action> registry) {
		return null;
	}
}
