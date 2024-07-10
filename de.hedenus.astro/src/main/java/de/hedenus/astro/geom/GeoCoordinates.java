package de.hedenus.astro.geom;

import de.hedenus.astro.common.Formats;

public class GeoCoordinates extends SphericalCoordinates
{
	private static final long serialVersionUID = -7726559381231408165L;

	public GeoCoordinates(final DecimalDegree longitude, final DecimalDegree latitude)
	{
		super(longitude, latitude);
	}

	public DecimalDegree getLongitude()
	{
		return (DecimalDegree) x;
	}

	public DecimalDegree getLatitude()
	{
		return (DecimalDegree) y;
	}

	@Override
	public String toString()
	{
		return "N " + Formats.formatDegrees(getLatitude().deg()) + " E " + Formats.formatDegrees(getLongitude().deg());
	}
}
