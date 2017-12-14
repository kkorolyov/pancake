package dev.kkorolyov.killstreek.media

import dev.kkorolyov.pancake.platform.math.Vector
import dev.kkorolyov.pancake.platform.media.CompositeImage
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector
import static java.lang.Math.round

class SpriteSpec extends Specification {
	@Shared int xFrames = 5
	@Shared int yFrames = 10
	@Shared float frameInterval = 0.1
	@Shared Vector orientationOffset = new Vector()
	Collection<Image> layers = (1..4).collect { Mock(Image) }
	CompositeImage image = Mock() {
		getSize() >> new Vector(1, 1)
		iterator() >> layers.iterator()
	}

	GraphicsContext g = GroovyMock()
	Vector position = randVector()

	Sprite sprite = new Sprite(image, orientationOffset, xFrames, yFrames, frameInterval)

	// Cannot mock final Java class
//	def "renders all image layers"() {
//		when:
//		sprite.render(g, position)
//
//		then:
//		layers.each {
//			1 * g.drawImage(it, _, _, _, _, _, _, _, _)
//		}
//	}

	def "steps 1 frame each frameInterval"() {
		when:
		sprite.tick(step as float)

		then:
		sprite.getFrame() == round(step / frameInterval)

		where:
		step << (frameInterval..<(frameInterval * xFrames * yFrames))
	}
	def "negative frameInterval steps backwards"() {
		sprite = new Sprite(image, orientationOffset, xFrames, yFrames, -frameInterval)

		when:
		sprite.tick(step as float)

		then:
		sprite.getFrame() == round(xFrames * yFrames - step / frameInterval)

		where:
		step << (frameInterval..<(frameInterval * xFrames * yFrames))
	}

	def "does not update if inactive"() {
		when:
		sprite.setActive(false)
		sprite.tick(frameInterval)

		then:
		sprite.getFrame() == 0
	}
}
