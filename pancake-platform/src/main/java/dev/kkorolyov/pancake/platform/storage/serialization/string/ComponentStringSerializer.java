package dev.kkorolyov.pancake.platform.storage.serialization.string;

import dev.kkorolyov.pancake.platform.storage.Attribute;
import dev.kkorolyov.pancake.platform.storage.Component;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;

import java.util.StringJoiner;

/**
 * Serializes components to strings.
 */
public class ComponentStringSerializer extends StringSerializer<Component> {
	private final AttributeStringSerializer attributeStringSerializer = new AttributeStringSerializer();

	public ComponentStringSerializer() {
		super(".+=.+");
	}

	@Override
	public Component read(String s) {
		String[] split = s.split("\\s*=\\s*", 2);
		String name = split[0], attributesS = split[1];

		Component component = new Component(name);
		for (String attributeS : attributesS.split(",\\s?(?!([^{]*}|[^\\[]*]))")) {	// Split on "," outside of "{...}" and "[...]"
			component.addAttribute(attributeStringSerializer.read(attributeS));
		}
		return component;
	}
	@Override
	public String write(Component component) {
		StringJoiner joiner = new StringJoiner(", ", (component.getName() + " = "), "");

		for (Attribute attribute : component.getAttributes()) {
			joiner.add(attributeStringSerializer.write(attribute));
		}
		return joiner.toString();
	}
}
