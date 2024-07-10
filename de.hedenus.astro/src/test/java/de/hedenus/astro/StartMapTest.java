package de.hedenus.astro;

import org.junit.jupiter.api.Test;

import de.hedenus.astro.common.Log;
import de.hedenus.astro.map.MapGeneration;

public class StartMapTest
{

	@Test
	public void test1()
	{
		long t0 = System.currentTimeMillis();

		new MapGeneration().generate().save("image1.png");

		Log.info("Done in " + ((System.currentTimeMillis() - t0) / 1000.0f) + "s");
	}

	@Test
	public void test2()
	{
		new MapGeneration().drawRaster().drawStars().drawConstellationLines().save("image2.png");
	}
}
