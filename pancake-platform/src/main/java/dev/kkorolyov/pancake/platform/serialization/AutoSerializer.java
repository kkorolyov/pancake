package dev.kkorolyov.pancake.platform.serialization;

import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Serializes using the most appropriate provider of some {@link Serializer} type.
 */
public class AutoSerializer<I, O, P extends Serializer<I, O>> implements Serializer<I, O> {
	private final ServiceLoader<P> providers;
	private final Function<O, RuntimeException> exceptionGenerator;

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
		return null;
	}

	@Override
	public boolean accepts(O out) {
		return providerStream()
				.anyMatch(provider -> provider.accepts(out));
	}

	protected I read(O out, Function<? super P, ? extends I> readFunction) {
		return providerStream()
				.filter(provider -> provider.accepts(out))
				.findFirst()
				.map(readFunction)
				.orElseThrow(() -> exceptionGenerator.apply(out));
	}

	private Stream<P> providerStream() {
		return StreamSupport.stream(providers.spliterator(), false);
	}
}
