package dev.kkorolyov.pancake.skillet.model

import java.util.Objects


/**
 * A container of distinct components.
 * @param name entity name
 * @param components entity components
 */
class GenericEntity(
		name: String = "",
		components: Iterable<GenericComponent> = emptyList()
) : Model<GenericEntity>() {
	var name: String = name
		set(value) {
			field = value
			changed(EntityChangeEvent.NAME)
		}
	private val _components: MutableMap<String, GenericComponent> = HashMap()
	val components: Iterable<GenericComponent> get() = _components.values

	init {
		components.forEach { this += it }
	}

	/**
	 * @param name component name to search by
	 * @return `true` if this entity contains a component of name `name`
	 */
	operator fun contains(name: String): Boolean = _components.containsKey(name)

	/** @param component component to add */
	operator fun plusAssign(component: GenericComponent) {
		_components[component.name] = component
		changed(EntityChangeEvent.ADD)
	}
	/** @param name name of component to remove */
	operator fun minusAssign(name: String) {
		val removed = _components.remove(name)
		if (removed !== null) changed(EntityChangeEvent.REMOVE)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other === null || !this::class.isInstance(other)) return false

		other as GenericEntity
		return name == other.name
				&& _components == other._components
	}
	override fun hashCode(): Int = Objects.hash(name, _components)

	/**
	 * A change to an entity.
	 */
	enum class EntityChangeEvent : ModelChangeEvent {
		NAME,
		ADD,
		REMOVE
	}
}
