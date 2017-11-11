package dev.kkorolyov.pancake.skillet.model;

import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;
import dev.kkorolyov.pancake.skillet.model.factory.ComponentFactory;
import dev.kkorolyov.pancake.skillet.serialization.GenericEntitySerializer;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;

/**
 * A self-contained context representing a single Skillet project.
 */
public class Workspace extends Model<Workspace> {
	private final ComponentFactory componentFactory = new ComponentFactory();

	private final Collection<GenericEntity> entities = new LinkedHashSet<>();
	private GenericEntity activeEntity;

	private final StringSerializer<GenericEntity> entitySerializer = new GenericEntitySerializer();

	/** @return workspace component factory */
	public ComponentFactory getComponentFactory() {
		return componentFactory;
	}

	/**
	 * Adds a new entity to the workspace.
	 * @return new entity
	 */
	public GenericEntity newEntity() {
		GenericEntity entity = new GenericEntity();
		addEntity(entity);

		return entity;
	}

	/** @return {@code true} if this workspace contains {@code entity} */
	public boolean containsEntity(GenericEntity entity) {
		return entities.contains(entity);
	}

	/**
	 * Adds an entity and sets it as the active entity.
	 * Adds all new components in {@code entity} to the component factory.
	 * @param entity entity to add
	 * @return {@code this}
	 */
	public Workspace addEntity(GenericEntity entity) {
		entities.add(entity);
		setActiveEntity(entity);

		componentFactory.add(entity.getComponents(), false);

		changed(WorkspaceChangeEvent.ADD);

		return this;
	}
	/**
	 * Attempts to remove an entity from the workspace.
	 * @param entity entity to remove
	 * @return {@code true} if this workspace contained {@code entity} and it was removed
	 */
	public boolean removeEntity(GenericEntity entity) {
		boolean result = entities.remove(entity);

		if (result) changed(WorkspaceChangeEvent.REMOVE);

		return result;
	}

	/** @return all entities in this workspace */
	public Iterable<GenericEntity> getEntities() {
		return entities;
	}

	/** @return last entity set as active */
	public Optional<GenericEntity> getActiveEntity() {
		return Optional.ofNullable(activeEntity);
	}
	/**
	 * @param entity entity to set as active
	 * @throws IllegalArgumentException if {@code entity} is not in this workspace
	 */
	public void setActiveEntity(GenericEntity entity) {
		if (Objects.equals(activeEntity, entity)) return;

		if (entity != null && !containsEntity(entity)) throw new IllegalArgumentException("Active entity must be in the workspace");

		this.activeEntity = entity;
		changed(WorkspaceChangeEvent.ACTIVE);
	}

	/**
	 * A change to a workspace.
	 */
	public enum WorkspaceChangeEvent implements ModelChangeEvent {
		ACTIVE,
		ADD,
		REMOVE
	}
}
