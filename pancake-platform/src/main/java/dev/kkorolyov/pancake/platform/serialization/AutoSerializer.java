package dev.kkorolyov.pancake.platform.serialization;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Serializes using the most appropriate provider of some {@link Serializer} type.
 */
public class AutoSerializer<I, O, P extends Serializer<I, O>> implements Serializer<I, O> {
	private final ServiceLoader<P> providers;
	private final Function<Object, RuntimeException> exceptionGenerator;

	/**
	 * Constructs a new auto serializer.
	 * @param providerType serializer type of which this serializer tries each provider
	 */
	public AutoSerializer(Class<P> providerType) {
		providers = ServiceLoader.load(providerType);
		exceptionGenerator = out -> new UnsupportedOperationException("No " + providerType.getSimpleName() + " accepts: " + out);
	}

	@Override
	public I read(O out) {
		return read(out, provider -> provider.read(out));
	}
	@Override
	public O write(I in) {
		return write(in, provider -> provider.write(in));
	}

	protected I read(O out, Function<? super P, ? extends I> readFunction) {
		return providerStream()
				.filter(provider -> provider.accepts(out))
				.findFirst()
				.map(readFunction)
				.orElseThrow(() -> exceptionGenerator.apply(out));
	}
	protected O write(I in, Function<? super P, ? extends O> writeFunction) {
		return getClosestProvider(in)
				.map(writeFunction)
				.orElseThrow(() -> exceptionGenerator.apply(in));
	}

	@Override
	public boolean accepts(O out) {
		return providerStream()
				.anyMatch(provider -> provider.accepts(out));
	}

	private Stream<P> providerStream() {
		return StreamSupport.stream(providers.spliterator(), false);
	}

	private Optional<P> getClosestProvider(I in) {
		NavigableMap<Integer, P> providerMap = new TreeMap<>();

		providerStream()
				.forEach(provider -> {
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
			if (parent.equals(c)) return Optional.of(degree);
			else degree++;
		} while ((c = c.getSuperclass()) != null);

		return Optional.empty();
	}
}
