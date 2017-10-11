package dev.kkorolyov.pancake.platform.storage.serialization.string;

import dev.kkorolyov.pancake.platform.storage.Attribute;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializers;

import java.util.ServiceLoader;

/**
 * Serializes attributes to strings.
 */
public class AttributeStringSerializer extends StringSerializer<Attribute> {
	private static final Iterable<StringSerializer> valueSerializers = ServiceLoader.load(StringSerializer.class);

	public AttributeStringSerializer() {
		super(".+:\\s.+");
	}

	@Override
	public Attribute read(String s) {
		String[] split = s.split(":\\s", 2);
		String name = split[0], valueS = split[1];

		return new Attribute(name, StringSerializers.read(valueS));
	}
	@Override
	public String write(Attribute attribute) {
		return attribute.getName() + ": " + attribute.getValue();
	}
}
