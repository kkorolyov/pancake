package dev.kkorolyov.killstreek.media

import dev.kkorolyov.pancake.platform.media.graphic.CompositeRenderable
import dev.kkorolyov.pancake.platform.media.graphic.Image
import dev.kkorolyov.pancake.platform.media.graphic.RenderTransform
import dev.kkorolyov.pancake.platform.media.graphic.Viewport

import spock.lang.Shared
import spock.lang.Specification

import static java.lang.Math.round

class SpriteSpec extends Specification {
	@Shared
	Viewport viewport = new Viewport(5, 10)
	@Shared
	long frameInterval = 1
	Image image = Mock()
	CompositeRenderable<Image> sheets = new CompositeRenderable<>(image)
	RenderTransform transform = new RenderTransform()

	Sprite sprite = new Sprite(sheets, viewport, frameInterval)

	def "renders sheets"() {
		when:
		sprite.render(transform)

		then:
		1 * image.render(transform)
	}

	def "steps 1 frame each frameInterval"() {
		when:
		sprite.tick(step)

		then:
		sprite.getFrame() == round(step / frameInterval)

		where:
		step << (frameInterval..<(frameInterval * viewport.length()))
	}
	def "negative frameInterval steps backwards"() {
		sprite = new Sprite(sheets, viewport, -frameInterval)

		when:
		sprite.tick(step)

		then:
		sprite.length() + sprite.getFrame() == round(viewport.length() - step / frameInterval)

		where:
		step << (frameInterval..<(frameInterval * viewport.length()))
	}

	def "does not update if inactive"() {
		when:
		sprite.setActive(false)
		sprite.tick(frameInterval)

		then:
		sprite.getFrame() == 0
	}
}
