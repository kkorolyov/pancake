package dev.kkorolyov.killstreek.media

import dev.kkorolyov.killstreek.component.Health
import dev.kkorolyov.pancake.platform.math.Vector
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Paint

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randVector

class HealthBarSpec extends Specification {
	@Shared Vector size = randVector()
	Health health = Mock()

	GraphicsContext g = GroovyMock() {
		getFill() >> Mock(Paint)
		getStroke() >> Mock(Paint)
		getLineWidth() >> randFloat()
	}
	Vector position = randVector()

	HealthBar healthBar = new HealthBar(health, size)

	// Cannot mock final Java class
//	def "renders rectangle"() {
//		when:
//		healthBar.render(g, position)
//
//		then:
//		1 * g.fillRect(position.getX(), position.getY(), _, _)
//	}
}
