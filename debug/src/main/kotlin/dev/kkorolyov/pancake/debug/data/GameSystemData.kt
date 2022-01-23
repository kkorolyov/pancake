package dev.kkorolyov.pancake.debug.data

import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.utility.Sampler
import javafx.beans.value.ObservableValue
import tornadofx.objectBinding
import tornadofx.objectProperty
import kotlin.math.roundToInt

data class GameSystemData(private val system: GameSystem? = null, private val sampler: Sampler? = null) :
	DynamicModel<GameSystemData> {
	val name: ObservableValue<String>
		get() = nameProperty
	val signature: ObservableValue<String>
		get() = signatureProperty
	val tick: ObservableValue<Long>
		get() = tickProperty
	val tps: ObservableValue<Int?> by lazy { tickProperty.objectBinding { it?.let { (1e9 / it).roundToInt() } } }

	private val nameProperty = objectProperty(if (system != null) system::class.simpleName else null)
	private val signatureProperty =
		objectProperty(system?.signature?.asSequence()?.map { it?.simpleName }?.joinToString())
	private val tickProperty = objectProperty<Long>()

	init {
		refresh()
	}

	override fun refresh() {
		sampler?.let {
			tickProperty.set(it.value)
		}
	}

	override fun bind(other: GameSystemData?) {
		other?.nameProperty?.let(nameProperty::bind) ?: nameProperty.unbind()
		other?.signatureProperty?.let(signatureProperty::bind) ?: signatureProperty.unbind()
		other?.tickProperty?.let(tickProperty::bind) ?: tickProperty.unbind()
	}
}
