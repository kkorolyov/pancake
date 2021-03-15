package dev.kkorolyov.pancake.javafx

import dev.kkorolyov.flopple.function.Memoizer.memoize
import dev.kkorolyov.pancake.javafx.render.EnhancedGraphicsContext
import dev.kkorolyov.pancake.javafx.render.JavaFxBox
import dev.kkorolyov.pancake.javafx.render.JavaFxImage
import dev.kkorolyov.pancake.javafx.render.JavaFxText
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

private fun Canvas.setSize(width: Double, height: Double) {
	this.width = width
	this.height = height

	// FIXME BAD
	Resources.RENDER_MEDIUM.camera.setSize(width, height)
}

/**
 * [Application] implemented through JavaFX.
 */
class JavaFxApplication : Application {
	private val cursor: Vector = Vector()
	private val inputs: MutableCollection<Enum<*>> = HashSet()

	override fun toInput(key: String): Enum<*> =
		try {
			KeyCode.valueOf(key)
		} catch (e: IllegalArgumentException) {
			MouseButton.valueOf(key)
		}

	override fun getCursor(): Vector = cursor
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

	fun attach(config: Config, gameLoop: GameLoop, cursor: Vector, inputs: MutableCollection<Enum<*>>) {
		Platform.runLater {
			scene.apply {
				onMouseMoved = EventHandler { cursor.set(it.x, it.y) }

				onMousePressed = EventHandler { inputs += it.button }
				onMouseReleased = EventHandler { inputs -= it.button }

				onKeyPressed = EventHandler { inputs += it.code }
				onKeyReleased = EventHandler { inputs -= it.code }
			}

			canvas.setSize(config.width, config.height)

			stage.apply {
				title = config.title
				icons += FxImage(config.iconUri)

				onCloseRequest = EventHandler { gameLoop.stop() }

				show()

				widthProperty().addListener { _, oldValue, newValue ->
					canvas.let {
						it.setSize(it.width + newValue.toDouble() - oldValue.toDouble(), it.height)
					}
				}
				heightProperty().addListener { _, oldValue, newValue ->
					canvas.let {
						it.setSize(it.width, it.height + newValue.toDouble() - oldValue.toDouble())
					}
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
	private val unitPixels = Vector(64.0, 64.0, 1.0)

	// FIXME Decouple camera
	private val camera: Camera = Camera(Vector(), Vector(unitPixels).apply { scale(Vector(1.0, -1.0, 1.0)) }, 0.0, 0.0)
	private val g: EnhancedGraphicsContext by lazy {
		EnhancedGraphicsContext(Runner.canvas.graphicsContext2D)
	}
	private val imageCache: (String) -> FxImage = memoize(Runner::getImage)::apply

	override fun getCamera(): Camera = camera

	override fun getImage(uri: String): Image = JavaFxImage(imageCache(uri), g)

	override fun getBox(): Box = JavaFxBox(unitPixels, g)
	override fun getText(): Text = JavaFxText(g)

	override fun invoke(renderAction: Runnable) {
		Platform.runLater(renderAction::run)
	}

	override fun clearInvoke(renderAction: Runnable) {
		Platform.runLater {
			g.get().clearRect(0.0, 0.0, Runner.canvas.width, Runner.canvas.height)

			renderAction.run()
		}
	}
}
