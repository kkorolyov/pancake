package dev.kkorolyov.pancake.skillet.model

import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.skillet.serialization.GenericComponentSerializer

/**
 * A map of attributes.
 * @param name component name
 * @param attributes component attributes
 */
data class GenericComponent(
		var name: String,
		val attributes: MutableMap<String, Any>
) : Model<GenericComponent>(), Component {
	/** @param attribute attribute to add */
	operator fun plusAssign(attribute: Pair<String, Any>) {
		attributes += attribute
	}
	/** @param name name of attribute to remove */
	operator fun minusAssign(name: String) {
		attributes.remove(name)
	}

	/**
	 * @param name name of attribute to get
	 * @return attribute value, if exists
	 */
	operator fun get(name: String): Any? = attributes[name]
	/**
	 * Adds an attribute.
	 * @param name attribute name
	 * @param value attribute value
	 */
	operator fun set(name: String, value: Any) {
		attributes[name] = value
	}

	fun copy(): GenericComponent = GenericComponentSerializer.read(GenericComponentSerializer.write(this))
}
