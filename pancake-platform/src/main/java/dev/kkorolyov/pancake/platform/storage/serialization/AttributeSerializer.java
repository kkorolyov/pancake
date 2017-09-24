package dev.kkorolyov.pancake.platform.storage.serialization;

import dev.kkorolyov.pancake.platform.storage.Attribute;

/**
 * Serializes attributes.
 */
public class AttributeSerializer implements Serializer<Attribute> {
	@Override
	public Attribute read(String s) {
		String[] split = s.split(":\\s", 2);
		String name = split[0], valueS = split[1];

		return new Attribute(name, ValueParsers.getStrategy(valueS).parse(valueS));
	}
	@Override
	public String write(Attribute attribute) {
		return attribute.getName() + ": " + attribute.getValue();
	}

}
