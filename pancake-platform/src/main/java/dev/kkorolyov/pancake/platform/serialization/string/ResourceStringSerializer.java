package dev.kkorolyov.pancake.platform.serialization.string;

import dev.kkorolyov.pancake.platform.Registry;
import dev.kkorolyov.pancake.platform.serialization.Serializer;

import java.util.regex.Pattern;

/**
 * Serializes {@link Registry} resource entries.
 * Reads and adds deserialized resources to the associated registry.
 * @param <K> key type
 * @param <V> resource type
 */
public final class ResourceStringSerializer<K, V> extends StringSerializer<V> {
	private static final Pattern SPLIT = Pattern.compile("\\s*=\\s*");

	private final Registry<K, ? super V> context;
	private final Serializer<K, ? super String> keySerializer;
	private final Serializer<V, ? super String> resourceSerializer;

	/**
	 * Constructs a new resource serializer.
	 * @param keySerializer serializes entry key
	 * @param resourceSerializer serializes entry resource
	 * @param context associated registry
	 */
	public ResourceStringSerializer(
			Serializer<K, ? super String> keySerializer,
			Serializer<V, ? super String> resourceSerializer,
			Registry<K, ? super V> context
	) {
		super("\\w+\\s*=.+");
		this.context = context;
		this.keySerializer = keySerializer;
		this.resourceSerializer = resourceSerializer;
	}

	@Override
	public V read(String out) {
		String[] split = SPLIT.split(out);
		K key = keySerializer.read(split[0]);
		V resource = resourceSerializer.read(split[1]);

		context.put(key, resource);

		return resource;
	}
	@Override
	public String write(V in) {
		return keySerializer.write(context.getKeys(in).iterator().next())
				+ "="
				+ resourceSerializer.write(in);
	}

	@Override
	public String toString() {
		return "ResourceStringSerializer{" +
				"context=" + context +
				", keySerializer=" + keySerializer +
				", resourceSerializer=" + resourceSerializer +
				'}';
	}
}
