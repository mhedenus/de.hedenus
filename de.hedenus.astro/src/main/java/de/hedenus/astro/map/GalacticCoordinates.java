package de.hedenus.astro.map;

import de.hedenus.astro.geom.Angle;
import de.hedenus.astro.geom.DecimalDegree;
import de.hedenus.astro.geom.SphericalCoordinates;

public class GalacticCoordinates
{
	public static final double alpha0 = Math.toRadians(192.8595);
	public static final double delta0 = Math.toRadians(27.1284);
	public static final double l0 = Math.toRadians(122.9320);

	public static SphericalCoordinates convert(final SphericalCoordinates eq)
	{
		double ra = eq.x().rad();
		double dec = eq.y().rad();

		double l = l0 - Math.atan2((Math.cos(dec) * Math.sin(ra - alpha0)), //
				(Math.sin(dec) * Math.cos(delta0) - Math.cos(dec) * Math.sin(delta0) * Math.cos(ra - alpha0)));

		double b = Math
				.asin(Math.sin(dec) * Math.sin(delta0) + Math.cos(dec) * Math.cos(delta0) * Math.cos(ra - alpha0));

		SphericalCoordinates ga = new SphericalCoordinates( //
				new DecimalDegree(Angle.fold360(Math.toDegrees(l))), //
				new DecimalDegree(Math.toDegrees(b)));
		return ga;
	}

	private GalacticCoordinates()
	{
		//NOP
	}
}
