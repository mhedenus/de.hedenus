package de.hedenus.astro.geom;

import java.io.Serializable;
import java.util.Objects;

import de.hedenus.astro.common.Formats;

public class SphericalCoordinates implements Serializable
{
	private static final long serialVersionUID = 6965263670948555845L;

	protected final Angle x;
	protected final Angle y;

	public SphericalCoordinates(final Angle x, final Angle y)
	{
		this.x = x;
		this.y = y;
	}

	public Angle x()
	{
		return x;
	}

	public Angle y()
	{
		return y;
	}

	/**
	 * Returns the great circle distance to p2 in degrees.
	 */
	public double distanceTo(final SphericalCoordinates p2)
	{
		SphericalCoordinates p1 = this;
		double rad = Math.acos(p1.y.sin() * p2.y.sin() + p1.y.cos() * p2.y.cos() * Math.cos(p2.x.rad() - p1.x.rad()));
		return Math.toDegrees(rad);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(final Object obj)
	{
		boolean result = this == obj;
		if (!result && obj instanceof SphericalCoordinates that)
		{
			result = this.x.equals(that.x) && this.y.equals(that.y);
		}
		return result;
	}

	@Override
	public String toString()
	{
		return "(" + Formats.formatDegrees(x.deg()) + "," + Formats.formatDegrees(y.deg()) + ")";
	}
}
