package de.hedenus.astro.common;

import de.hedenus.astro.geom.DecimalDegree;
import de.hedenus.astro.geom.GeoCoordinates;
import de.hedenus.astro.geom.Location;

public class Defaults
{
	public static final Location REGENSBURG = new Location("Regensburg", //
			new GeoCoordinates(new DecimalDegree(12.096944), //
					new DecimalDegree(49.017222)));

	public static Location LOCATION = REGENSBURG;

	private Defaults()
	{
		//NOP
	}
}
