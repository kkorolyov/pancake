package dev.kkorolyov.pancake.javafx

import dev.kkorolyov.flopple.function.Memoizer.memoize
import dev.kkorolyov.pancake.javafx.render.EnhancedGraphicsContext
import dev.kkorolyov.pancake.javafx.render.JavaFxBox
import dev.kkorolyov.pancake.javafx.render.JavaFxImage
import dev.kkorolyov.pancake.javafx.render.JavaFxText
import dev.kkorolyov.pancake.platform.GameLoop
import dev.kkorolyov.pancake.platform.math.Vector2
import dev.kkorolyov.pancake.platform.math.Vectors
import dev.kkorolyov.pancake.platform.media.Camera
import dev.kkorolyov.pancake.platform.media.graphic.Image
import dev.kkorolyov.pancake.platform.media.graphic.shape.Box
import dev.kkorolyov.pancake.platform.media.graphic.shape.Text
import dev.kkorolyov.pancake.platform.plugin.Application
import dev.kkorolyov.pancake.platform.plugin.Application.Config
import dev.kkorolyov.pancake.platform.plugin.Plugins
import dev.kkorolyov.pancake.platform.plugin.RenderMedium
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.stage.Stage
import java.nio.file.Path
import javafx.application.Application as FxApplication
import javafx.scene.image.Image as FxImage

/**
 * [Application] implemented through JavaFX.
 */
class JavaFxApplication : Application {
	private val cursor: Vector2 = Vectors.create(0.0, 0.0)
	private val inputs: MutableCollection<Enum<*>> = HashSet()

	override fun toInput(key: String): Enum<*> =
		try {
			KeyCode.valueOf(key)
		} catch (e: IllegalArgumentException) {
			MouseButton.valueOf(key)
		}

	override fun getCursor(): Vector2 = cursor
	override fun getInputs(): Collection<Enum<*>> = inputs

	override fun execute(config: Config, gameLoop: GameLoop) {
		// Start FX app and long-lived objects
		Runner.attach(config, gameLoop, cursor, inputs)
	}
}

/**
 * JavaFX application.
 */
private object Runner : FxApplication() {
	lateinit var stage: Stage
	lateinit var scene: Scene
	val canvas: Canvas = Canvas()

	init {
		Platform.startup {
			start(Stage())
		}
	}

	fun attach(config: Config, gameLoop: GameLoop, cursor: Vector2, inputs: MutableCollection<Enum<*>>) {
		val renderMedium = Plugins.renderMedium()

		Platform.runLater {
			scene.apply {
				onMouseMoved = EventHandler {
					cursor.x = it.x
					cursor.y = it.y
				}

				onMousePressed = EventHandler { inputs += it.button }
				onMouseReleased = EventHandler { inputs -= it.button }

				onKeyPressed = EventHandler { inputs += it.code }
				onKeyReleased = EventHandler { inputs -= it.code }
			}

			canvas.width = config.width
			canvas.height = config.height
			// FIXME BAD
			renderMedium.camera.setSize(config.width, config.height)

			stage.apply {
				title = config.title
				icons += FxImage(config.iconUri)

				onCloseRequest = EventHandler { gameLoop.stop() }

				show()

				widthProperty().addListener { _, oldValue, newValue ->
					canvas.width += newValue.toDouble() - oldValue.toDouble()
					renderMedium.camera.setSize(canvas.width, canvas.height)
				}
				heightProperty().addListener { _, oldValue, newValue ->
					canvas.height += newValue.toDouble() - oldValue.toDouble()
					renderMedium.camera.setSize(canvas.width, canvas.height)
				}

				Thread(gameLoop::start).start()
			}
		}
	}

	fun getImage(path: String) = FxImage(Path.of(path).toUri().toString())

	override fun start(primaryStage: Stage) {
		scene = Scene(Group(canvas))
		stage = primaryStage.also { it.scene = scene }
	}
}

/**
 * [RenderMedium] implemented through JavaFX.
 */
class JavaFxRenderMedium : RenderMedium {
	// FIXME Make this dynamic
	private val unitPixels = 64.0

	// FIXME Decouple camera
	private val camera: Camera = Camera(Vectors.create(0.0, 0.0), unitPixels, 0.0, 0.0)
	private val g: EnhancedGraphicsContext by lazy {
		EnhancedGraphicsContext(Runner.canvas.graphicsContext2D)
	}
	private val imageCache: (String) -> FxImage = memoize(Runner::getImage)::apply

	override fun getCamera(): Camera = camera

	override fun getImage(uri: String): Image = JavaFxImage(imageCache(uri), g)

	override fun getBox(): Box = JavaFxBox(unitPixels, g)
	override fun getText(): Text = JavaFxText(g)

	override fun invoke(renderAction: Runnable) {
		Platform.runLater(renderAction)
	}

	override fun clearInvoke(renderAction: Runnable) {
		Platform.runLater {
			g.get().clearRect(0.0, 0.0, Runner.canvas.width, Runner.canvas.height)

			renderAction.run()
		}
	}
}
