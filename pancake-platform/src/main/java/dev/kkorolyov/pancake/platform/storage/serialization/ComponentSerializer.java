package dev.kkorolyov.pancake.platform.storage.serialization;

import dev.kkorolyov.pancake.platform.storage.Attribute;
import dev.kkorolyov.pancake.platform.storage.Component;

import java.util.StringJoiner;

/**
 * Serializes components.
 */
public class ComponentSerializer implements Serializer<Component> {
	private final AttributeSerializer attributeSerializer = new AttributeSerializer();

	@Override
	public Component read(String s) {
		String[] split = s.split("\\s*=\\s*", 2);
		String name = split[0], attributesS = split[1];

		Component component = new Component(name);
		for (String attributeS : attributesS.split(",\\s?(?!([^{]*}|[^\\[]*]))")) {	// Split on "," outside of "{...}" and "[...]"
			component.addAttribute(attributeSerializer.read(attributeS));
		}
		return component;
	}
	@Override
	public String write(Component component) {
		StringJoiner joiner = new StringJoiner(", ", (component.getName() + " = "), "");

		for (Attribute attribute : component.getAttributes()) {
			joiner.add(attributeSerializer.write(attribute));
		}
		return joiner.toString();
	}
}
