package dev.kkorolyov.pancake.core.registry;

import dev.kkorolyov.flopple.function.convert.Converter;
import dev.kkorolyov.pancake.core.action.ForceAction;
import dev.kkorolyov.pancake.core.action.TransformAction;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.registry.Registry;
import dev.kkorolyov.pancake.platform.registry.ResourceReaderFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * {@link ResourceReaderFactory.ActionResource} for core actions.
 */
public class ActionResourceReaderFactory implements ResourceReaderFactory.ActionResource {
	private static final Pattern NUMBER_PATTERN = Pattern.compile("[+-]?(\\d*\\.)?\\d+([eE][+-]?\\d+)?");
	private static final Pattern VECTOR_PATTERN = Pattern.compile(String.format("%s(,\\s*%s)*", NUMBER_PATTERN, NUMBER_PATTERN));

	private static final Pattern FORCE_PATTERN = Pattern.compile(String.format("(?i)FORCE(?-i)\\(%s\\)", VECTOR_PATTERN));
	private static final Pattern TRANSFORM_PATTERN = Pattern.compile(String.format("(?i)TRANSFORM(?-i)\\(%s(\\s*\\|\\s*%s)?\\)", VECTOR_PATTERN, VECTOR_PATTERN));

	private static Converter<String, Optional<ForceAction>> force() {
		return Converter.selective(
				in -> FORCE_PATTERN.matcher(in).matches(),
				in -> matchVectors(in)
						.map(ForceAction::new)
						.findFirst()
						.orElseThrow(() -> new IllegalStateException("found no vectors in a matched pattern"))
		);
	}
	private static Converter<String, Optional<TransformAction>> transform() {
		return Converter.selective(
				in -> TRANSFORM_PATTERN.matcher(in).matches(),
				in -> {
					List<Vector> vectors = matchVectors(in)
							.collect(toList());

					return new TransformAction(vectors.get(0), vectors.size() > 1 ? vectors.get(1) : null);
				}
		);
	}

	private static Stream<Vector> matchVectors(String in) {
		List<String> vectors = VECTOR_PATTERN.matcher(in).results()
				.map(MatchResult::group)
				.collect(toList());
		return vectors.stream()
				.map(NUMBER_PATTERN::matcher)
				.map(Matcher::results)
				.map(matchResults -> matchResults
						.map(MatchResult::group)
						.map(BigDecimal::new)
						.mapToDouble(BigDecimal::doubleValue)
						.toArray()
				)
				.map(Vector::new);
	}

	@Override
	public Converter<String, Optional<? extends Action>> get(Registry<? super String, ? extends Action> registry) {
		return Converter.reducing(
				force(),
				transform()
		);
	}
}
