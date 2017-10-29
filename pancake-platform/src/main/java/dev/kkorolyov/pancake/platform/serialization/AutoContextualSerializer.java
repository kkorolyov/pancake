package dev.kkorolyov.pancake.platform.serialization;

/**
 * Serializes using the most appropriate provider of some {@link ContextualSerializer} type.
 */
public class AutoContextualSerializer<I, O, C, P extends ContextualSerializer<I, O, C>> extends AutoSerializer<I, O, P> implements ContextualSerializer<I, O, C> {
	/**
	 * Constructs a new auto contextual serializer.
	 * @param providerType contextual serializer type of which this contextual serializer tries each provider
	 */
	public AutoContextualSerializer(Class<P> providerType) {
		super(providerType);
	}

	@Override
	public I read(O out, C context) {
		return read(out, provider -> provider.read(out, context));
	}
	@Override
	public O write(I in, C context) {
		return write(in, provider -> provider.write(in, context));
	}
}
