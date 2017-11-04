package dev.kkorolyov.pancake.platform.serialization;

import dev.kkorolyov.pancake.platform.Resources;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Serializes using the most appropriate provider of some {@link Serializer} type.
 */
public class AutoSerializer<I, O, P extends Serializer<I, O>> implements Serializer<I, O> {
	private static final Map<ProviderKey, Collection<? extends Serializer>> providerMap = new HashMap<>();

	private final Function<Object, RuntimeException> exceptionGenerator;
	private final Class<P> providerType;
	private final Object[] providerParameters;
	private Collection<P> providers;

	/**
	 * Constructs a new auto serializer.
	 * @param providerType serializer type of which this serializer tries each provider
	 * @param parameters constructor parameters to instantiate all providers with
	 */
	public AutoSerializer(Class<P> providerType, Object... parameters) {
		this.providerType = providerType;
		this.providerParameters = parameters;
		exceptionGenerator = out -> new UnsupportedOperationException("No " + providerType.getSimpleName() + " accepts: " + out);
	}

	@Override
	public I read(O out) {
		return providers()
				.filter(provider -> provider.accepts(out))
				.findFirst()
				.map(provider -> provider.read(out))
				.orElseThrow(() -> exceptionGenerator.apply(out));
	}
	@Override
	public O write(I in) {
		return getNearestProvider(in)
				.map(provider -> provider.write(in))
				.orElseThrow(() -> exceptionGenerator.apply(in));
	}

	@Override
	public boolean accepts(O out) {
		return providers()
				.anyMatch(provider -> provider.accepts(out));
	}

	private Optional<P> getNearestProvider(I in) {
		NavigableMap<Integer, P> providerMap = new TreeMap<>();

		providers().forEach(provider -> {
			Type providerType = ((ParameterizedType) provider.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			Class<?> inType = in.getClass();

			computeDegree(providerType, inType)
					.ifPresent(degree -> providerMap.put(degree, provider));
		});
		return Optional.ofNullable(providerMap.firstEntry())
				.map(Entry::getValue);
	}
	private Optional<Integer> computeDegree(Type parent, Class<?> child) {
		int degree = 0;
		Class<?> c = child;

		do {
			if (parent.equals(c)) { return Optional.of(degree); } else degree++;
		} while ((c = c.getSuperclass()) != null);

		return Optional.empty();
	}

	private Stream<P> providers() {
		if (providers == null) providers = (Collection<P>) providerMap.computeIfAbsent(new ProviderKey(providerType, providerParameters), k -> Resources.providers(k.providerType, k.parameters));

		return providers.stream();
	}

	private static class ProviderKey {
		final Class<? extends Serializer> providerType;
		final Object[] parameters;

		ProviderKey(Class<? extends Serializer> providerType, Object[] parameters) {
			this.providerType = providerType;
			this.parameters = parameters;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null || !getClass().isAssignableFrom(obj.getClass())) return false;

			ProviderKey o = (ProviderKey) obj;
			return Objects.equals(providerType, o.providerType)
					&& Arrays.equals(parameters, o.parameters);
		}
		@Override
		public int hashCode() {
			return Objects.hash(providerType, Objects.hash(parameters));
		}
	}
}
