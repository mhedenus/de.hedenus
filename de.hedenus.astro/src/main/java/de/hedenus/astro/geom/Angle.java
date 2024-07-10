package de.hedenus.astro.geom;

import java.io.Serializable;

public abstract class Angle implements Comparable<Angle>, Serializable
{
	private static final long serialVersionUID = -5421249427213935238L;

	public static double fold360(final double deg)
	{
		if (deg < 0.0)
		{
			int n = (int) (deg / 360.0) - 1;
			return (deg - n * 360.0);
		}
		else if (deg > 360.0)
		{
			int n = (int) (deg / 360.0);
			return (deg - n * 360.0);
		}
		else
		{
			return deg;
		}
	}

	private final double deg;

	protected Angle(final double deg)
	{
		this.deg = deg;
	}

	public double deg()
	{
		return deg;
	}

	public double rad()
	{
		return Math.toRadians(deg);
	}

	public double sin()
	{
		return Math.sin(rad());
	}

	public double cos()
	{
		return Math.cos(rad());
	}

	@Override
	public int compareTo(final Angle that)
	{
		return Double.compare(this.deg, that.deg);
	}

	public boolean equals(final Angle that, final double eps)
	{
		return Math.abs(this.deg - that.deg) <= eps;
	}

	@Override
	public boolean equals(final Object obj)
	{
		boolean result = this == obj;
		if (!result && obj instanceof Angle)
		{
			result = this.compareTo((Angle) obj) == 0;
		}
		return result;
	}

	@Override
	public int hashCode()
	{
		long bits = Double.doubleToLongBits(deg);
		return (int) (bits ^ (bits >>> 32));
	}
}