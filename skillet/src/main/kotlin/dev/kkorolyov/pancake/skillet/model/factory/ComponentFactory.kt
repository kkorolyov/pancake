package dev.kkorolyov.pancake.skillet.model.factory

import dev.kkorolyov.pancake.skillet.model.GenericComponent
import dev.kkorolyov.pancake.skillet.model.Model

/**
 * Provides fresh instances of components by name.
 * @param components base components
 */
data class ComponentFactory(private val components: MutableMap<String, GenericComponent> = HashMap()) : Model<ComponentFactory>() {
	/** Names of all components */
	val names: Iterable<String> get() = components.keys

	/** @return whether this factory contains a component with name `name` */
	operator fun contains(name: String): Boolean = name in components

	/**
	 * @param name name of component to copy
	 * @return copy of component with name `name`, or `null` if no such component
	 */
	operator fun get(name: String): GenericComponent? = components[name]?.copy()

	/**
	 * [add] without overwriting.
	 */
	operator fun plusAssign(component: GenericComponent) {
		add(component, false)
	}
	/**
	 * [add] without overwriting.
	 */
	operator fun plusAssign(components: Iterable<GenericComponent>) {
		add(components, false)
	}

	/**
	 * @param component component to add
	 * @param overwrite `true` allows overwriting an existing component with the same name
	 */
	fun add(component: GenericComponent, overwrite: Boolean) {
		if (overwrite || component.name !in this) {
			components[component.name] = component
			changed(ComponentFactoryChangeEvent.ADD)
		}
	}
	/**
	 * Adds multiple components using [add].
	 */
	fun add(components: Iterable<GenericComponent>, overwrite: Boolean) {
		for (component in components) add(component, overwrite)
	}

	/** @param name name of component to remove */
	operator fun minusAssign(name: String) {
		val removed = components.remove(name)
		if (removed !== null) changed(ComponentFactoryChangeEvent.REMOVE)
	}
	/** @param names names of components to remove */
	operator fun minusAssign(names: Iterable<String>) {
		for (name in names) this -= name
	}

	/**
	 * Removes all components.
	 */
	fun clear() {
		this -= names
	}

	/**
	 * A change to a component factory.
	 */
	enum class ComponentFactoryChangeEvent : ModelChangeEvent {
		ADD,
		REMOVE
	}
}
