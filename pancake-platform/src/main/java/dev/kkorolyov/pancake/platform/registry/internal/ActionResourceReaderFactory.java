package dev.kkorolyov.pancake.platform.registry.internal;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.CollectiveAction;
import dev.kkorolyov.pancake.platform.action.MultiStageAction;
import dev.kkorolyov.pancake.platform.registry.Registry;
import dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory;
import dev.kkorolyov.simplefuncs.convert.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

import static dev.kkorolyov.simplefuncs.function.Memoizer.memoize;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * {@link ResourceReaderFactory.ActionResource} provided by the pancake platform.
 */
public final class ActionResourceReaderFactory implements ResourceReaderFactory.ActionResource {
	private static final Pattern REFERENCE_PATTERN = Pattern.compile("\\w+");

	private static final Pattern COLLECTIVE_PATTERN = Pattern.compile("\\[.+(,\\s*.+)*]");
	private static final Pattern COLLECTIVE_SPLIT_PATTERN = Pattern.compile(",\\s*");

	private static final Pattern MULTI_STAGE_PATTERN = Pattern.compile("\\{.*(,.*){2}}");
	private static final Pattern MULTI_STAGE_SPLIT_PATTERN = Pattern.compile(",\\s*(?![^\\[]*])");
	private static final long MULTI_STAGE_HOLD_THRESHOLD = (long) (Double.parseDouble(Config.config().get("holdThreshold")) * 1e9);

	private final Function<? super Registry<? super String, ? extends Action>, ? extends Converter<? super String, ? extends Optional<? extends Action>>> autoConverter = memoize(registry -> ResourceReaderFactory.get(ActionResource.class, registry));

	private static Converter<String, Optional<Action>> reference(Registry<? super String, ? extends Action> registry) {
		return Converter.selective(
				in -> REFERENCE_PATTERN.matcher(in).matches(),
				registry::get
		);
	}
	private Converter<String, Optional<CollectiveAction>> collective(Registry<? super String, ? extends Action> registry) {
		return Converter.selective(
				in -> COLLECTIVE_PATTERN.matcher(in).matches(),
				in -> new CollectiveAction(
						Arrays.stream(COLLECTIVE_SPLIT_PATTERN.split(in.substring(1, in.length() - 1)))
								.map(autoConverter.apply(registry)::convert)
								.flatMap(Optional::stream)
								.collect(toSet())
				)
		);
	}
	private Converter<String, Optional<MultiStageAction>> multiStage(Registry<? super String, ? extends Action> registry) {
		return Converter.selective(
				in -> MULTI_STAGE_PATTERN.matcher(in).matches(),
				in -> {
					List<Action> actions = Arrays.stream(MULTI_STAGE_SPLIT_PATTERN.split(in.substring(1, in.length() - 1)))
							.map(autoConverter.apply(registry)::convert)
							.map(optional -> optional.orElse(null))
							.collect(toList());

					return new MultiStageAction(
							actions.get(0),
							actions.get(1),
							actions.get(2),
							MULTI_STAGE_HOLD_THRESHOLD
					);
				}
		);
	}

	@Override
	public Converter<String, Optional<? extends Action>> get(Registry<? super String, ? extends Action> registry) {
		return Converter.reducing(
				reference(registry),
				collective(registry),
				multiStage(registry)
		);
	}
}
