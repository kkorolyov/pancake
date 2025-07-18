package dev.kkorolyov.pancake.platform.entity.io;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;

import java.util.stream.StreamSupport;

/**
 * Serializes {@link EntityPool}s.
 */
public final class EntityPoolSerializer implements Serializer<EntityPool> {
	@Override
	public void write(EntityPool value, WriteContext context) {
		var entities = StreamSupport.stream(value.spliterator(), false).toList();

		context.putInt(entities.size());
		for (Entity entity : entities) {
			context.putString(entity.getDebugNameOverride());

			context.putInt(entity.size());
			for (Component component : entity) {
				context.putString(component.getClass().getName());
				context.putObject(component);
			}
		}
	}
	@Override
	public EntityPool read(ReadContext context) {
		var result = new EntityPool();

		var poolSize = context.getInt();
		for (int i = 0; i < poolSize; i++) {
			var entity = result.create();
			entity.setDebugNameOverride(context.getString());

			var entitySize = context.getInt();
			for (int j = 0; j < entitySize; j++) {
				var clsName = context.getString();
				try {
					entity.put(context.getObject((Class<Component>) Class.forName(clsName)));
				} catch (ClassNotFoundException e) {
					throw new IllegalArgumentException("cannot read serialized component: %s".formatted(clsName), e);
				}
			}
		}

		return result;
	}

	@Override
	public Class<EntityPool> getType() {
		return EntityPool.class;
	}
}
