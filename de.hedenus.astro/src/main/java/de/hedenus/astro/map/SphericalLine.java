package de.hedenus.astro.map;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import de.hedenus.astro.geom.SphericalCoordinates;

public class SphericalLine implements Serializable
{
	private static final long serialVersionUID = -549513943676750630L;

	private final SphericalCoordinates[] x;

	public SphericalLine(final SphericalCoordinates x0, final SphericalCoordinates x1)
	{
		this.x = x0.x().compareTo(x1.x()) < 0 ? new SphericalCoordinates[] { x0, x1 }
				: new SphericalCoordinates[] { x1, x0 };
	}

	public SphericalCoordinates x0()
	{
		return x[0];
	}

	public SphericalCoordinates x1()
	{
		return x[1];
	}

	@Override
	public int hashCode()
	{
		return Objects.hash((Object[]) x);
	}

	@Override
	public boolean equals(final Object obj)
	{
		boolean result = this == obj;
		if (!result && obj instanceof SphericalLine that)
		{
			result = Arrays.equals(this.x, that.x);
		}
		return result;
	}

}
