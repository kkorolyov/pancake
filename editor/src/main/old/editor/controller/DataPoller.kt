package dev.kkorolyov.pancake.editor.controller

import dev.kkorolyov.pancake.editor.data.EntityData
import dev.kkorolyov.pancake.editor.data.GameSystemData
import dev.kkorolyov.pancake.platform.Config
import dev.kkorolyov.pancake.platform.GameEngine
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.utility.Sampler
import javafx.beans.value.ObservableDoubleValue
import javafx.beans.value.ObservableIntegerValue
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.bind
import tornadofx.doubleProperty
import tornadofx.intProperty
import tornadofx.objectProperty
import tornadofx.observableListOf
import tornadofx.observableMapOf
import tornadofx.observableSetOf
import tornadofx.onChange
import tornadofx.runLater
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

/**
 * Regularly polls and maintains information from a registered [GameLoop].
 * Exposes polled information as observable values.
 * Polls at rate set in platform [Config.get] as `editor.pollRate` - target number of polls per second, else defaults to `30` polls per second.
 */
class DataPoller : Controller() {
	val entities: ObservableList<EntityData> by lazy {
		observableListOf<EntityData>().apply {
			bind(entitySet, ::EntityData)
			FXCollections.sort(this, compareBy { it.id.value })
		}
	}
	val systems: ObservableList<GameSystemData> by lazy {
		observableListOf<GameSystemData>().apply {
			bind(systemsMap) { gameSystem, sampler -> GameSystemData(gameSystem, sampler) }
			FXCollections.sort(this, compareBy { it.name.value })
		}
	}
	val tps: ObservableIntegerValue
		get() = tpsProperty
	val speed: ObservableDoubleValue
		get() = speedProperty
	val engine: ObservableValue<GameEngine>
		get() = engineProperty

	private val entitySet = observableSetOf<Entity>()
	private val systemsMap = observableMapOf<GameSystem, Sampler>()
	private val tpsProperty = intProperty()
	private val speedProperty = doubleProperty()
	private val engineProperty = objectProperty<GameEngine>().apply {
		onChange {
			clear()
			it?.let {
				pollTask = pollExecutorFactory.scheduleAtFixedRate(
					{ refresh(it) },
					0,
					(1e9 / (Config.get().getProperty("editor.pollRate")?.toLong() ?: 30)).toLong(),
					TimeUnit.NANOSECONDS
				)
			}
		}
	}

	private val pollExecutorFactory = Executors.newScheduledThreadPool(1, object : ThreadFactory {
		private val defaultFactory = Executors.defaultThreadFactory()

		override fun newThread(r: Runnable): Thread {
			val thread = defaultFactory.newThread(r)
			thread.isDaemon = true
			return thread
		}
	})
	private var pollTask: ScheduledFuture<*>? = null

	/**
	 * Registers [engine] with this poller.
	 */
	fun register(engine: GameEngine) {
		engineProperty.set(engine)
	}

	private fun refresh(engine: GameEngine) {
		runLater {
			val currentEntities = engine.entities.toSet()
			entitySet.retainAll(currentEntities)
			entitySet.addAll(currentEntities)

			systemsMap.keys.retainAll(engine.perfMonitor.systems.keys)
			systemsMap.putAll(engine.perfMonitor.systems)

			tpsProperty.set((1e9 / engine.perfMonitor.engine.value).roundToInt())
			speedProperty.set(engine.speed)

			systems.forEach(GameSystemData::refresh)
			entities.forEach(EntityData::refresh)
		}
	}

	private fun clear() {
		pollTask?.cancel(true)
		pollTask = null

		entitySet.clear()
		systemsMap.clear()
		tpsProperty.set(0)
	}
}
