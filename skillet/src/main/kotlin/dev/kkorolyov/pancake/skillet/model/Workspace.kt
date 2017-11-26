package dev.kkorolyov.pancake.skillet.model

import dev.kkorolyov.pancake.skillet.model.factory.ComponentFactory
import java.util.Collections
import java.util.IdentityHashMap
import java.util.Objects

/**
 * A self-contained context representing a single Skillet project.
 */
class Workspace : Model<Workspace>() {
	/** Attached component factory */
	val componentFactory: ComponentFactory = ComponentFactory()

	private val _entities: MutableCollection<GenericEntity> = Collections.newSetFromMap(IdentityHashMap())
	/** All entities */
	val entities: Iterable<GenericEntity> get() = _entities
	/** Components of all entities */
	val components: Iterable<GenericComponent> get() = entities.flatMap(GenericEntity::components)

	var activeEntity: GenericEntity? = null
		/**
		 * @param entity entity to set as active
		 * @throws IllegalArgumentException if `entity` is not in this workspace
		 */
		set(entity) {
			if (field === entity) return

			if (entity !== null && entity !in this) throw IllegalArgumentException("Active entity ($entity) must be in the workspace")

			field = entity
			changed(WorkspaceChangeEvent.ACTIVE)
		}

	/**
	 * Adds all entities from a workspace.
	 * @param workspace workspace with entities to add
	 * @return `this`
	 */
	fun addWorkspace(workspace: Workspace): Workspace {
		workspace._entities.forEach { add(it) }
		return this
	}

	/**
	 * Creates and adds a new entity.
	 * @return new entity
	 */
	fun newEntity(): GenericEntity {
		val entity = GenericEntity()
		add(entity)

		return entity
	}

	/** @return `true` if this workspace contains `entity` */
	operator fun contains(entity: GenericEntity): Boolean = _entities.contains(entity)

	/** @see [add] */
	operator fun plusAssign(entity: GenericEntity) {
		add(entity)
	}
	/**
	 * Adds an entity and sets it as the active entity.
	 * Adds all new components in `entity` to the component factory.
	 * @param entity entity to add
	 * @return `this`
	 */
	fun add(entity: GenericEntity): Workspace {
		_entities.add(entity)
		activeEntity = entity

		componentFactory.add(entity.components, false)

		changed(WorkspaceChangeEvent.ADD)

		return this
	}
	/**
	 * Attempts to remove an entity from this workspace.
	 * @param entity entity to remove
	 * @return `true` if this workspace contained `entity` and it was removed
	 */
	fun removeEntity(entity: GenericEntity): Boolean {
		val result = _entities.remove(entity)

		if (result) changed(WorkspaceChangeEvent.REMOVE)

		return result
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other === null || !this::class.isInstance(other)) return false

		other as Workspace
		return componentFactory == other.componentFactory
				&& _entities == other._entities
				&& activeEntity == other.activeEntity
	}
	override fun hashCode(): Int = Objects.hash(componentFactory,
			_entities,
			activeEntity)

	/**
	 * A change to a workspace.
	 */
	enum class WorkspaceChangeEvent : ModelChangeEvent {
		ACTIVE,
		ADD,
		REMOVE
	}
}
