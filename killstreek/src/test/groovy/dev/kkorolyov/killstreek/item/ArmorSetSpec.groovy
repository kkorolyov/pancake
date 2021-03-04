package dev.kkorolyov.killstreek.item

import dev.kkorolyov.killstreek.media.Sprite
import dev.kkorolyov.pancake.platform.math.BoundedValue

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randInt
import static dev.kkorolyov.pancake.platform.SpecUtilities.randString

class ArmorSetSpec extends Specification {
	@Shared Sprite sprite = Mock()
	@Shared BoundedValue<Integer> unbroken = new BoundedValue<>(0, 1, 1)
	@Shared BoundedValue<Integer> broken = new BoundedValue<>(0, 0, 0)
	@Shared Armor[] unbrokenArmor = Armor.Type.values().collect {it -> new Armor(randString(), sprite, unbroken, randInt(), it)}
	@Shared Armor[] brokenArmor = Armor.Type.values().collect {it -> new Armor(randString(), sprite, broken, randInt(), it)}

	ArmorSet armorSet = new ArmorSet()

	def "sums defense of all unbroken equipped pieces"() {
		when:
		armor.each {armorSet.equip(it)}

		then:
		armorSet.getDefense() == armor.sum {it -> it.isBroken() ? 0 : it.getValue()}

		where:
		armor << [unbrokenArmor, brokenArmor]
	}

	def "returns all armor"() {
		boolean swap = false;
		Armor[] armor = Armor.Type.values().collect {
			new Armor(randString(), sprite, (swap = !swap) ? unbroken : broken, randInt(), it)
		}

		when:
		armor.each {armorSet.equip(it)}

		then:
		armorSet.getArmor().size() == armor.size()
		armorSet.getArmor().containsAll(armor)
	}

	def "equipping new piece of an equipped type removes and returns previous such piece"() {
		Armor oldArmor = new Armor(randString(), sprite, unbroken, randInt(), type)
		Armor newArmor = new Armor(randString(), sprite, broken, randInt(), type)

		when:
		armorSet.equip(oldArmor)

		then:
		armorSet.equip(newArmor) == oldArmor
		armorSet.getArmor().size() == 1
		armorSet.getArmor().contains(newArmor)

		where:
		type << Armor.Type.values()
	}
	def "equipping new piece of a new type returns null"() {
		expect:
		armorSet.equip(new Armor(randString(), sprite, unbroken, randInt(), type)) == null

		where:
		type << Armor.Type.values()
	}
}
