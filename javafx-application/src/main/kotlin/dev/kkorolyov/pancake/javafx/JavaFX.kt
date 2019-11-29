package dev.kkorolyov.pancake.javafx

import dev.kkorolyov.pancake.javafx.render.EnhancedGraphicsContext
import dev.kkorolyov.pancake.javafx.render.JavaFXBox
import dev.kkorolyov.pancake.javafx.render.JavaFXImage
import dev.kkorolyov.pancake.javafx.render.JavaFXText
import dev.kkorolyov.pancake.platform.GameLoop
import dev.kkorolyov.pancake.platform.Resources
import dev.kkorolyov.pancake.platform.application.Application
import dev.kkorolyov.pancake.platform.application.Application.Config
import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.media.Camera
import dev.kkorolyov.pancake.platform.media.graphic.Image
import dev.kkorolyov.pancake.platform.media.graphic.RenderMedium
import dev.kkorolyov.pancake.platform.media.graphic.shape.Box
import dev.kkorolyov.pancake.platform.media.graphic.shape.Text
import dev.kkorolyov.simplefuncs.function.Memoizer
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.stage.Stage
import java.util.function.Function

private val canvas: Canvas = Canvas()
private val scene: Scene = Scene(Group(canvas))

/**
 * [Application] implemented through JavaFX.
 */
class JavaFXApplication : Application {
	private val cursor: Vector = Vector()
	private val inputs: MutableCollection<Enum<*>> = HashSet()

	init {
		with(scene) {
			onMouseMoved = EventHandler { this@JavaFXApplication.cursor.set(it.x, it.y) }

			onMousePressed = EventHandler { inputs += it.button }
			onMouseReleased = EventHandler { inputs -= it.button }

			onKeyPressed = EventHandler { inputs += it.code }
			onKeyReleased = EventHandler { inputs -= it.code }
		}
	}

	override fun toInput(key: String): Enum<*> =
			try {
				KeyCode.valueOf(key)
			} catch (e: IllegalArgumentException) {
				MouseButton.valueOf(key)
			}

	override fun getCursor(): Vector = cursor
	override fun getInputs(): Collection<Enum<*>> = inputs

	override fun execute(config: Config, gameLoop: GameLoop) {
		Runner(config, gameLoop)
	}
}
private class Runner(private val config: Config, private val gameLoop: GameLoop) : javafx.application.Application() {
	override fun start(primaryStage: Stage) {
		setSize(config.width, config.height)

		primaryStage.scene = scene
		with(primaryStage) {
			title = config.title
			icons += javafx.scene.image.Image(config.iconUri)

			show()

			widthProperty().addListener { _, oldValue, newValue ->
				setSize(canvas.width + newValue.toDouble() - oldValue.toDouble(), canvas.height)
			}
			heightProperty().addListener { _, oldValue, newValue ->
				setSize(canvas.width, canvas.height + newValue.toDouble() - oldValue.toDouble())
			}

			onCloseRequest = EventHandler { gameLoop.stop() }

			Thread(gameLoop::start).start()
		}
	}

	private fun setSize(width: Double, height: Double) {
		canvas.width = width
		canvas.height = height

		Resources.RENDER_MEDIUM.camera.setSize(width, height)
	}
}

/**
 * [RenderMedium] implemented through JavaFX.
 */
class JavaFXRenderMedium : RenderMedium {
	private val camera: Camera = Camera(Vector(), Vector(), 0.0, 0.0)
	private val g: EnhancedGraphicsContext = EnhancedGraphicsContext(canvas.graphicsContext2D)
	private val imageRetriever: Function<in String, out javafx.scene.image.Image> = Memoizer.memoize { uri -> javafx.scene.image.Image(uri) }

	override fun getCamera(): Camera = camera

	override fun getImage(uri: String): Image = JavaFXImage(imageRetriever.apply(uri), g)

	override fun getBox(): Box = JavaFXBox(g)
	override fun getText(): Text = JavaFXText(g)

	override fun clear() {
		g.get().clearRect(0.0, 0.0, canvas.width, canvas.height)
	}
}
