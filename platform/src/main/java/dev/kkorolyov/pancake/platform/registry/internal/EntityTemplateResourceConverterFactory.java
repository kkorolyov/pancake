package dev.kkorolyov.pancake.platform.registry.internal;

import dev.kkorolyov.flub.function.convert.Converter;
import dev.kkorolyov.pancake.platform.entity.EntityTemplate;
import dev.kkorolyov.pancake.platform.registry.Resource;
import dev.kkorolyov.pancake.platform.registry.ResourceConverterFactory;

import java.util.Map;
import java.util.Optional;

/**
 * {@link ResourceConverterFactory} for basic entity templates.
 */
public final class EntityTemplateResourceConverterFactory implements ResourceConverterFactory<EntityTemplate> {
	@Override
	public Converter<Object, Optional<Resource<EntityTemplate>>> get() {
		return Converter.selective(
				t -> t instanceof Map,
				t -> Resource.constant(EntityTemplate.read((Map<String, Object>) t))
		);
	}

	@Override
	public Class<EntityTemplate> getType() {
		return EntityTemplate.class;
	}
}
