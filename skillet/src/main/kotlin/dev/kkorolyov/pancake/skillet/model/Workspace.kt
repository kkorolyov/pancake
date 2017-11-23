package dev.kkorolyov.pancake.skillet.model

import dev.kkorolyov.pancake.skillet.model.factory.ComponentFactory
import java.util.*

/**
 * A self-contained context representing a single Skillet project.
 */
class Workspace : Model<Workspace>() {
	val componentFactory: ComponentFactory = ComponentFactory()

	private val entities: MutableCollection<GenericEntity> = LinkedHashSet()
	var activeEntity: GenericEntity? = null
		/**
		 * @param entity entity to set as active
		 * @throws IllegalArgumentException if `entity` is not in this workspace
		 */
		set(entity) {
			if (field == entity) return

			if (entity != null && entity !in this) throw IllegalArgumentException("Active entity ($entity) must be in the workspace")

			field = entity
			changed(WorkspaceChangeEvent.ACTIVE)
		}

	/**
	 * Adds all entities from a workspace.
	 * @param workspace workspace with entities to add
	 * @return `this`
	 */
	fun addWorkspace(workspace: Workspace): Workspace {
		workspace.entities.forEach { add(it) }
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
	operator fun contains(entity: GenericEntity): Boolean = entities.contains(entity)

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
		entities.add(entity)
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
		val result = entities.remove(entity)

		if (result) changed(WorkspaceChangeEvent.REMOVE)

		return result
	}

	/** @return all entities */
	fun getEntities(): Iterable<GenericEntity> = entities
	/** @return all components of all entities */
	fun getComponents(): Iterable<GenericComponent> = entities.flatMap(GenericEntity::components)

	override fun equals(other: Any?): Boolean {
		if (this == other) return true
		if (other == null || !this::class.isInstance(other)) return false

		other as Workspace
		return componentFactory == other.componentFactory
				&& entities == other.entities
				&& activeEntity == other.activeEntity
	}
	override fun hashCode(): Int = Objects.hash(componentFactory,
			entities,
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
