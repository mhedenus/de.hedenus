package de.hedenus.astro.geom;

public class HorizontalCoordinates extends CelestialCoordinates
{
	private static final long serialVersionUID = -1746490381964371060L;

	public HorizontalCoordinates(final DecimalDegree azimuth, final DecimalDegree altitude)
	{
		super(azimuth, altitude);
	}

	public DecimalDegree azimuth()
	{
		return (DecimalDegree) x;
	}

	public DecimalDegree altitude()
	{
		return (DecimalDegree) y;
	}
}
