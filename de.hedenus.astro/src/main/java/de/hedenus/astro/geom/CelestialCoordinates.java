package de.hedenus.astro.geom;

public abstract class CelestialCoordinates extends SphericalCoordinates
{
	private static final long serialVersionUID = -2488217446435336464L;

	protected CelestialCoordinates(final Angle x, final Angle y)
	{
		super(x, y);
	}
}
