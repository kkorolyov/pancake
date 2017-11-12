package dev.kkorolyov.pancake.skillet.model;

import dev.kkorolyov.pancake.skillet.model.factory.ComponentFactory;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A self-contained context representing a single Skillet project.
 */
public class Workspace extends Model<Workspace> {
	private final ComponentFactory componentFactory = new ComponentFactory();

	private final Collection<GenericEntity> entities = new LinkedHashSet<>();
	private GenericEntity activeEntity;

	/** @return workspace component factory */
	public ComponentFactory getComponentFactory() {
		return componentFactory;
	}

	/**
	 * Adds all entities in a workspace to this workspace.
	 * @param workspace workspace with entities to add
	 * @return {@code this}
	 */
	public Workspace addWorkspace(Workspace workspace) {
		workspace.streamEntities()
				.forEach(this::addEntity);
		return this;
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
	/** @return stream over all entities */
	public Stream<GenericEntity> streamEntities() {
		return entities.stream();
	}

	/** all components of all entities */
	public Iterable<GenericComponent> getComponents() {
		return streamComponents().collect(Collectors.toList());
	}
	/** @return stream over all components of all entities */
	public Stream<GenericComponent> streamComponents() {
		return streamEntities().flatMap(GenericEntity::streamComponents);
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !getClass().isAssignableFrom(obj.getClass())) return false;

		Workspace o = (Workspace) obj;
		return Objects.equals(componentFactory, o.componentFactory)
				&& Objects.equals(entities, o.entities)
				&& Objects.equals(activeEntity, o.activeEntity);
	}
	@Override
	public int hashCode() {
		return Objects.hash(componentFactory,
				entities,
				activeEntity);
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
