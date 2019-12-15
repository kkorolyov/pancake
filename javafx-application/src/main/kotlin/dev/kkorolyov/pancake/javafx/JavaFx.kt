package dev.kkorolyov.pancake.javafx

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
import dev.kkorolyov.simplefuncs.function.Memoizer.memoize
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

// Shared singletons
private val canvas: Canvas = Canvas()
private val scene: Scene = Scene(Group(canvas))

private var stage: Stage? = null

private fun Canvas.setSize(width: Double, height: Double) {
	this.width = width
	this.height = height

	Resources.RENDER_MEDIUM.camera.setSize(width, height)
}

/**
 * [Application] implemented through JavaFX.
 */
class JavaFxApplication : Application {
	private val cursor: Vector = Vector()
	private val inputs: MutableCollection<Enum<*>> = HashSet()

	init {
		scene.apply {
			onMouseMoved = EventHandler { this@JavaFxApplication.cursor.set(it.x, it.y) }

			onMousePressed = EventHandler { inputs += it.button }
			onMouseReleased = EventHandler { inputs -= it.button }

			onKeyPressed = EventHandler { inputs += it.code }
			onKeyReleased = EventHandler { inputs -= it.code }
		}

		// Launch to get a handle to stage
		Thread { FxApplication.launch(Runner::class.java) }.start()
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
		canvas.setSize(config.width, config.height)

		stage?.apply {
			title = config.title
			icons += FxImage(config.iconUri)

			onCloseRequest = EventHandler { gameLoop.stop() }

			Platform.runLater {
				show()

				widthProperty().addListener { _, oldValue, newValue ->
					canvas.setSize(canvas.width + newValue.toDouble() - oldValue.toDouble(), canvas.height)
				}
				heightProperty().addListener { _, oldValue, newValue ->
					canvas.setSize(canvas.width, canvas.height + newValue.toDouble() - oldValue.toDouble())
				}
			}

			Thread(gameLoop::start).start()
		} ?: throw IllegalStateException("Stage has not been initialized")
	}
}

/**
 * JavaFX application runner.
 */
class Runner : FxApplication() {
	override fun start(primaryStage: Stage) {
		primaryStage.scene = scene
		stage = primaryStage
	}
}

/**
 * [RenderMedium] implemented through JavaFX.
 */
class JavaFxRenderMedium : RenderMedium {
	private val camera: Camera = Camera(Vector(), Vector(64.0, -64.0, 1.0), 0.0, 0.0)
	private val g: EnhancedGraphicsContext = EnhancedGraphicsContext(canvas.graphicsContext2D)
	private val imageCache: (String) -> FxImage = memoize<String, FxImage> { FxImage(Path.of(it).toUri().toString()) }::apply

	override fun getCamera(): Camera = camera

	override fun getImage(uri: String): Image = JavaFxImage(imageCache(uri), g)

	override fun getBox(): Box = JavaFxBox(g)
	override fun getText(): Text = JavaFxText(g)

	override fun invoke(renderAction: Runnable) {
		Platform.runLater {
			g.get().clearRect(0.0, 0.0, canvas.width, canvas.height)

			renderAction.run()
		}
	}
}
