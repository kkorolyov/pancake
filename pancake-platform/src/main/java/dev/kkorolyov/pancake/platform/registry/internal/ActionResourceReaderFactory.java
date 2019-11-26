package dev.kkorolyov.pancake.platform.registry.internal;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.CollectiveAction;
import dev.kkorolyov.pancake.platform.action.KeyAction;
import dev.kkorolyov.pancake.platform.action.MultiStageAction;
import dev.kkorolyov.pancake.platform.registry.Registry;
import dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory;
import dev.kkorolyov.simplefuncs.convert.Converter;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
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

	private static final Pattern KEY_PATTERN = Pattern.compile("\\([_a-zA-Z]+(,\\s*[_a-zA-Z]+)*\\)\\s*=\\s*.+");
	private static final Pattern KEY_SPLIT_PATTERN = Pattern.compile("\\s*=\\s*");
	private static final Pattern KEY_SPLIT_KEY_PATTERN = Pattern.compile(",\\s*");

	private final Function<? super Registry<? super String, ? extends Action>, ? extends Converter<String, ? extends Optional<? extends Action>>> autoConverter;

	/**
	 * Constructs a new action resource reader factory.
	 */
	public ActionResourceReaderFactory() {
		this(memoize(registry -> ResourceReaderFactory.reduce(ActionResource.class, registry)));
	}
	private ActionResourceReaderFactory(Function<? super Registry<? super String, ? extends Action>, ? extends Converter<String, ? extends Optional<? extends Action>>> autoConverter) {
		this.autoConverter = autoConverter;
	}

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
							.map(actionS -> actionS.length() <= 0 ? null : autoConverter.apply(registry).convert(actionS))
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
	private Converter<String, Optional<KeyAction>> key(Registry<? super String, ? extends Action> registry) {
		return Converter.selective(
				in -> KEY_PATTERN.matcher(in).matches(),
				in -> {
					String[] split = KEY_SPLIT_PATTERN.split(in, 2);
					String keysS = split[0], actionS = split[1];

					Action action = autoConverter.apply(registry).convert(actionS)
							.orElse(null);

					return new KeyAction(
							action instanceof MultiStageAction
									? (MultiStageAction) action
									: new MultiStageAction(action, null, null, 0),  // Hold threshold irrelevant
							Arrays.stream(KEY_SPLIT_KEY_PATTERN.split(keysS.substring(1, keysS.length() - 1)))
									.map(s -> {
										try {
											return KeyCode.valueOf(s);
										} catch (IllegalArgumentException e) {
											return MouseButton.valueOf(s);
										}
									}).collect(toList())
					);
				}
		);
	}

	@Override
	public Converter<String, Optional<? extends Action>> get(Registry<? super String, ? extends Action> registry) {
		return Converter.reducing(
				reference(registry),
				collective(registry),
				multiStage(registry),
				key(registry)
		);
	}
}
