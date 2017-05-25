package dev.kkorolyov.pancake.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
@SuppressWarnings("javadoc")
public class VectorTest {
	@Parameter(0)
	public float x;
	@Parameter(1)
	public float y;
	@Parameter(2)
	public float z;
	
	@Parameters(name = "({0}, {1}, {2})")
	public static Iterable<Object[]> data() {
		List<Object[]> randComponents = new LinkedList<>();
		
		for (int i = 0; i < 10; i++)
			randComponents.add(new Float[]{	randFloat(),
																			randFloat(),
																			randFloat()});
		
		return randComponents;
	}
	
	@Test
	public void testVector() {
		assertEquals(new Vector(0, 0, 0), new Vector());
	}
	@Test
	public void testVectorFloatFloat() {
		assertEquals(new Vector(x, y, 0), new Vector(x, y));
	}
	@Test
	public void testVectorFloatFloatFloat() {
		Vector vector = new Vector(x, y, z);
			
		assertEquals(x, vector.getX(), 0);
		assertEquals(y, vector.getY(), 0);
		assertEquals(z, vector.getZ(), 0);
	}
	@Test
	public void testVectorVector() {
		Vector expected = new Vector(x, y, z);
		
		assertEquals(expected, new Vector(expected));
	}
	
	@Test
	public void testTranslateFloatFloat() {
		Vector original = new Vector(x, y);
		float dx = randFloat(),
					dy = randFloat();
		float newX = original.getX() + dx,
					newY = original.getY() + dy;
		
		original.translate(dx, dy);

		assertEquals(new Vector(newX, newY), original);
	}
	@Test
	public void testTranslateFloatFloatFloat() {
		Vector original = new Vector(x, y, z);
		float dx = randFloat(),
					dy = randFloat(),
					dz = randFloat();
		float newX = original.getX() + dx,
					newY = original.getY() + dy,
					newZ = original.getZ() + dz;
		
		original.translate(dx, dy, dz);

		assertEquals(new Vector(newX, newY, newZ), original);
	}

	@Test
	public void testScaleFloat() {
		Vector original = new Vector(x, y, z);
		float scale = randFloat();
		float newX = original.getX() * scale,
					newY = original.getY() * scale,
					newZ = original.getZ() * scale;
		
		original.scale(scale);
		
		assertEquals(new Vector(newX, newY, newZ), original);
	}

	@Test
	public void testScaleVector() {
		Vector 	original = new Vector(x, y, z),
						scale = new Vector(randFloat(), randFloat(), randFloat());
		float newX = original.getX() * scale.getX(),
					newY = original.getY() * scale.getY(),
					newZ = original.getZ() * scale.getZ();
		
		original.scale(scale);
		
		assertEquals(new Vector(newX, newY, newZ), original);
	}

	@Test
	public void testAddVector() {
		Vector 	original = new Vector(x, y, z),
						translated = new Vector(original),
						added = new Vector(randFloat(), randFloat(), randFloat());
		
		original.add(added);
		translated.translate(added.getX(), added.getY(), added.getZ());
		
		assertEquals(translated, original);
	}

	@Test
	public void testAddVectorScale() {
		Vector 	original = new Vector(x, y, z),
						translated = new Vector(original),
						added = new Vector(randFloat(), randFloat(), randFloat());
		float scale = randFloat();

		original.add(added, scale);
		translated.translate(added.getX() * scale, added.getY() * scale, added.getZ() * scale);
		
		assertEquals(translated, original);
	}

	@Test
	public void testSubVector() {
		Vector 	original = new Vector(x, y, z),
						translated = new Vector(original),
						subbed = new Vector(randFloat(), randFloat(), randFloat());
		
		original.sub(subbed);
		translated.translate(-subbed.getX(), -subbed.getY(), -subbed.getZ());
		
		assertEquals(translated, original);
	}

	@Test
	public void testSubVectorScaled() {
		Vector 	original = new Vector(x, y, z),
						translated = new Vector(original),
						subbed = new Vector(randFloat(), randFloat(), randFloat());
		float scale = randFloat();
		
		original.sub(subbed, scale);
		translated.translate(-subbed.getX() * scale, -subbed.getY() * scale, -subbed.getZ() * scale);
		
		assertEquals(translated, original);
	}

	@Test
	public void testDot() {
		Vector 	v1 = new Vector(x, y, z),
						v2 = new Vector(randFloat(), randFloat(), randFloat());
		
		assertEquals(v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ(), v1.dot(v2), 0);
	}

	@Test
	public void testProject() {
		fail("Not yet implemented");
	}

	@Test
	public void testAngle() {
		fail("Not yet implemented");
	}

	@Test
	public void testNormalize() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUnitVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMagnitude() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDirection2D() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetFloatFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetFloatFloatFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetX() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetX() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetY() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetY() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetZ() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetZ() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}
	
	private static float randFloat() {
		Random rand = new Random();
		return (rand.nextBoolean() ? -1 : 1) * rand.nextFloat() * rand.nextInt(100);
	}
}
