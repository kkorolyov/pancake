package dev.kkorolyov.pancake.platform.media

import javafx.scene.image.Image

import spock.lang.Specification

class CompositeImageSpec extends Specification {
	Image[] images = (0..10).collect {it -> Mock(Image)}

	CompositeImage compositeImage = new CompositeImage(images)

	def "contains all constructor-set images in original order"() {
		expect:
		compositeImage.collect() == images
	}

	def "adds images the end"() {
		Image nextImage = Mock()
		List<Image> newImages = images.toList()
		newImages << nextImage

		when:
		compositeImage.add(nextImage)

		then:
		compositeImage.collect() != images
		compositeImage.collect() == newImages
	}

	def "adds all images from CompositeImage in order"() {
		List<Image> combined = images.toList()
		combined.addAll(images)

		when:
		compositeImage.add(new CompositeImage(images))

		then:
		compositeImage.collect() != images
		compositeImage.collect() == combined
	}
}
