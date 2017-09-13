package dev.kkorolyov.pancake.muffin.persistence;

import dev.kkorolyov.pancake.muffin.data.type.Attribute;
import dev.kkorolyov.pancake.muffin.persistence.value.ValueParsers;

/**
 * Deals with persistence of attributes.
 */
public class AttributePersister extends DataPersister<Attribute> {
	@Override
	public Attribute read(String s) {
		String[] split = s.split(":\\s?", 2);
		String name = split[0], valueS = split[1];

		return new Attribute(name, ValueParsers.getStrategy(valueS).parse(valueS));
	}
	@Override
	public String write(Attribute attribute) {
		return attribute.getName() + ": " + attribute.getValue();
	}

}
