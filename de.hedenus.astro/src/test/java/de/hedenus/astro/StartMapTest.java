package de.hedenus.astro;

import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import de.hedenus.astro.map.StarCatalogue;

public class StartMapTest
{

	@Test
	public void test5()
	{
		StarCatalogue starCatalogue = new StarCatalogue();
		System.out.println(starCatalogue.entries().stream().filter(e -> e.isStar()).map(e -> e.name())
				.collect(Collectors.toCollection(() -> new TreeSet<>())));

	}
}
