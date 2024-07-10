package de.hedenus.astro.geom;

import java.io.Serializable;

import de.hedenus.astro.common.Formats;

public class Region<P extends SphericalCoordinates> implements Serializable
{
	private static final long serialVersionUID = -8902916624170165303L;

	private static final int X = 0;
	private static final int Y = 1;
	private final double min[] = new double[2];
	private final double max[] = new double[2];

	public Region(final P a, final P b)
	{
		this.min[X] = a.x.deg();
		this.min[Y] = a.y.deg();

		this.max[X] = b.x.deg();
		this.max[Y] = b.y.deg();

		if (min[X] > max[X])
		{
			double t = min[X];
			min[X] = max[X];
			max[X] = t;
		}

		if (min[Y] > max[Y])
		{
			double t = min[Y];
			min[Y] = max[Y];
			max[Y] = t;
		}
	}

	public boolean contains(final P p)
	{
		final double x = p.x.deg();
		final double y = p.y.deg();

		return min[X] <= x && x <= max[X] //
				&& min[Y] <= y && y <= max[Y];
	}

	@Override
	public String toString()
	{
		return "(" + Formats.formatDegrees(min[X]) + "," + Formats.formatDegrees(min[Y]) + ") " + //
				"- (" + Formats.formatDegrees(max[X]) + "," + Formats.formatDegrees(max[Y]) + ")";
	}
}
