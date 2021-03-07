package dev.kkorolyov.pancake.platform.registry.internal;

import dev.kkorolyov.flopple.function.convert.Converter;
import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.CollectiveAction;
import dev.kkorolyov.pancake.platform.action.MultiStageAction;
import dev.kkorolyov.pancake.platform.registry.Deferred;
import dev.kkorolyov.pancake.platform.registry.DeferredConverterFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

import static dev.kkorolyov.flopple.function.Memoizer.memoize;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * {@link DeferredConverterFactory.ActionStrat} provided by the pancake platform.
 */
public final class ActionStratDeferredConverterFactory implements DeferredConverterFactory.ActionStrat {
	private static final Collection<String> MULTI_STAGE_KEYS = Set.of("start", "hold", "end");
	private static final long MULTI_STAGE_HOLD_THRESHOLD = (long) (Double.parseDouble(Config.get().getProperty("holdThreshold")) * 1e9);

	private final Supplier<Converter<Object, Optional<Deferred<String, Action>>>> autoReader = memoize(() -> DeferredConverterFactory.get(ActionStrat.class));

	private static Converter<Object, Optional<Deferred<String, Action>>> reference() {
		return Converter.selective(
				in -> in instanceof String,
				in -> Deferred.derived(singleton(in.toString()), resolver -> resolver.apply(in.toString()))
		);
	}
	private Converter<Object, Optional<Deferred<String, Action>>> collective() {
		return Converter.selective(
				in -> in instanceof Iterable,
				in -> {
					Collection<Deferred<String, Action>> subResources = StreamSupport.stream(((Iterable<?>) in).spliterator(), false)
							.map(autoReader.get()::convert)
							.map(opt -> opt.orElseThrow(() -> new IllegalArgumentException("No resource reader matches: " + opt)))
							.collect(toList());
					return Deferred.derived(
							getDependencies(subResources),
							resolver -> new CollectiveAction(
									subResources.stream()
											.map(subResource -> subResource.resolve(resolver))
											::iterator
							)
					);
				}
		);
	}
	private Converter<Object, Optional<Deferred<String, Action>>> multiStage() {
		return Converter.selective(
				in -> in instanceof Map && ((Map<?, ?>) in).keySet().stream().allMatch(key -> MULTI_STAGE_KEYS.contains(key.toString().toLowerCase())),
				in -> {
					Map<String, Deferred<String, Action>> subResources = ((Map<?, ?>) in).entrySet().stream()
							.collect(toMap(
									e -> e.getKey().toString(),
									e -> autoReader.get().convert(e.getValue()).orElseThrow(() -> new IllegalArgumentException("No resource reader matches: " + e.getValue()))
							));

					return Deferred.derived(
							getDependencies(subResources.values()),
							resolver -> new MultiStageAction(
									Optional.ofNullable(subResources.get("start"))
											.map(subResource -> subResource.resolve(resolver))
											.orElse(null),
									Optional.ofNullable(subResources.get("hold"))
											.map(subResource -> subResource.resolve(resolver))
											.orElse(null),
									Optional.ofNullable(subResources.get("end"))
											.map(subResource -> subResource.resolve(resolver))
											.orElse(null),
									MULTI_STAGE_HOLD_THRESHOLD
							)
					);
				}
		);
	}

	private Iterable<String> getDependencies(Collection<Deferred<String, Action>> subResources) {
		return subResources.stream()
				.map(Deferred::getDependencies)
				.flatMap(Collection::stream)
				::iterator;
	}
	@Override
	public Converter<Object, Optional<Deferred<String, Action>>> get() {
		return Converter.reducing(
				reference(),
				collective(),
				multiStage()
		);
	}
}
