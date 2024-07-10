package de.hedenus.astro;

import org.junit.jupiter.api.Test;

import de.hedenus.astro.map.ConstellationBoundaries;
import de.hedenus.astro.map.MapGeneration;

public class StartMapTest
{

	@Test
	public void test3()
	{
		for (Constellation c : Constellation.values())
		{
			ConstellationBoundaries.boundaries(c).sketch();
		}
	}

	@Test
	public void test4()
	{
		new MapGeneration(1000).animate();
	}
}
