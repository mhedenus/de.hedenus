package de.hedenus.astro;

import java.awt.Dimension;

import org.junit.jupiter.api.Test;

import de.hedenus.astro.map.MapProjection;
import de.hedenus.astro.map.Settings;
import de.hedenus.astro.map.StarCatalogue;

public class StartMapTest
{
	@Test
	public void test4()
	{
		System.out.println(new StarCatalogue().stars(Settings.defaultSettings(0, false).starMaxMagnitude).count());
	}

	@Test
	public void test5()
	{
		MapProjection mapProjection = new MapProjection(new Dimension(2000, 1000));

		System.out.println(mapProjection.inverseMollweide(500, 500));
		System.out.println(mapProjection.inverseMollweide(750, 500));
		System.out.println(mapProjection.inverseMollweide(1000, 500));
		System.out.println(mapProjection.inverseMollweide(1500, 500));
		System.out.println(mapProjection.inverseMollweide(1750, 500));

		System.out.println(mapProjection.inverseMollweide(1000, 250));
		System.out.println(mapProjection.inverseMollweide(1000, 750));
	}
}
