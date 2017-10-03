package dev.kkorolyov.pancake.platform.media

import dev.kkorolyov.simpleprops.Properties
import javafx.scene.image.Image

import spock.lang.Shared
import spock.lang.Specification

class ImagePoolSpec extends Specification {
	@Shared String oldImageName = "oldImage"
	@Shared String newImageName = "newImage"
	@Shared Properties props = new Properties()
	@Shared Image base = Mock()

	ImagePool images = new ImagePool()

	def setupSpec() {
		props.put(newImageName, oldImageName)
	}

	def "parses existing image first"() {
		CompositeImage oldImage = new CompositeImage(base)

		when:
		images.put(oldImageName, oldImage)
		images.put(props)

		then:
		images.get(newImageName).toList() == oldImage.toList()	// Identical base images
	}

	def "parses filename when nonexistent image"() {
		when:
		images.put(props)

		then:
		thrown(IllegalArgumentException)	// Attempts to parse invalid URL
	}
}
