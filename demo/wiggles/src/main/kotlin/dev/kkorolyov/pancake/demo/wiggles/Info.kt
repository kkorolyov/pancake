package dev.kkorolyov.pancake.demo.wiggles

import dev.kkorolyov.pancake.core.component.Transform
import dev.kkorolyov.pancake.core.component.movement.Velocity
import dev.kkorolyov.pancake.platform.Config
import dev.kkorolyov.pancake.platform.GameSystem
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.utility.Sampler
import javafx.beans.property.LongProperty
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.collections.FXCollections
import javafx.scene.control.TableView
import javafx.scene.control.skin.TableViewSkinBase
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.RowConstraints
import tornadofx.Controller
import tornadofx.View
import tornadofx.column
import tornadofx.gridpane
import tornadofx.gridpaneConstraints
import tornadofx.hbox
import tornadofx.label
import tornadofx.listview
import tornadofx.longProperty
import tornadofx.onChange
import tornadofx.resizeColumnsToFitContent
import tornadofx.runLater
import tornadofx.tableview
import java.io.OutputStream
import java.io.PrintStream
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong

class PropagatingPrintStream(out: OutputStream, private val propagate: MutableCollection<Any?>) : PrintStream(out) {
	override fun println(x: Any?) {
		super.println(x)
		runLater { propagate += x }
	}
}

class InfoController : Controller() {
	val tps: LongProperty = longProperty(0)
	val systems = FXCollections.observableArrayList<Map.Entry<GameSystem, Sampler>>()
	val entities = FXCollections.observableArrayList<Entity>()
	val logs = FXCollections.observableArrayList<Any>()

	init {
		Executors.newScheduledThreadPool(1) {
			Thread(it).apply { isDaemon = true }
		}.scheduleAtFixedRate(
			{
				runLater {
					tps.set((1e9 / gameEngine.perfMonitor.engine.value).roundToLong())
					systems.setAll(gameEngine.perfMonitor.systems.entries)
					entities.setAll(dev.kkorolyov.pancake.demo.wiggles.entities.toList())
				}
			},
			0,
			(1e3 / Config.get().getProperty("pollRate").toLong()).toLong(),
			TimeUnit.MILLISECONDS
		)

		System.setOut(PropagatingPrintStream(System.out, logs))
		System.setErr(PropagatingPrintStream(System.err, logs))
		println("Start logs")
	}
}

class TpsView : View() {
	val controller: InfoController by inject()

	override val root = hbox {
		label(controller.tps)
	}
}

class SystemsView : View() {
	val controller: InfoController by inject()

	override val root = tableview(controller.systems) {
		title = "Systems"
		column<Map.Entry<GameSystem, Sampler>, String>("System") { ReadOnlyObjectWrapper(it.value.key::class.java.simpleName) }
		column<Map.Entry<GameSystem, Sampler>, Long>("TPS") { ReadOnlyObjectWrapper((1e9 / it.value.value.value).roundToLong()) }
		column<Map.Entry<GameSystem, Sampler>, Long>("Tick time (ns)") { ReadOnlyObjectWrapper(it.value.value.value) }

		columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
	}
}

class EntitiesView : View() {
	val controller: InfoController by inject()

	override val root = tableview(controller.entities) {
		column<Entity, Int>("ID") { ReadOnlyObjectWrapper(it.value.id) }
		column<Entity, String>("Position") { ReadOnlyObjectWrapper(it.value[Transform::class.java]?.position?.let(this@EntitiesView::prettyPrint)) }
		column<Entity, String>("Velocity") { ReadOnlyObjectWrapper(it.value[Velocity::class.java]?.value?.let(this@EntitiesView::prettyPrint)) }

		columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
	}

	private fun prettyPrint(vector: Vector3) = String.format("(%.3f, %.3f, %.3f)", vector.x, vector.y, vector.z)
}

class LogsView : View() {
	val controller: InfoController by inject()

	override val root = listview(controller.logs)
}

class InfoView : View() {
	val tpsView: TpsView by inject()
	val systemsView: SystemsView by inject()
	val entitiesView: EntitiesView by inject()
	val logsView: LogsView by inject()

	override val root = gridpane {
		add(tpsView.root.gridpaneConstraints {
			columnRowIndex(0, 0)
			columnSpan = 2
		})
		add(systemsView.root.gridpaneConstraints {
			columnRowIndex(0, 1)
		})
		add(entitiesView.root.gridpaneConstraints {
			columnRowIndex(1, 1)
		})
		add(logsView.root.gridpaneConstraints {
			columnRowIndex(0, 2)
			columnSpan = 2
		})

		columnConstraints += listOf(
			ColumnConstraints().apply {
				percentWidth = 40.0
			},
			ColumnConstraints().apply {
				percentWidth = 60.0
			}
		)
		rowConstraints += listOf(
			RowConstraints(),
			RowConstraints().apply {
				percentHeight = 60.0
			},
			RowConstraints().apply {
				percentHeight = 40.0
			}
		)
	}
}
