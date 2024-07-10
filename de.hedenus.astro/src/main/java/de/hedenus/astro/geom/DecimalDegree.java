package de.hedenus.astro.geom;

import de.hedenus.astro.common.Formats;

public class DecimalDegree extends Angle
{
	private static final long serialVersionUID = 8973339644769401871L;

	public DecimalDegree(final double deg)
	{
		super(deg);
	}

	@Override
	public String toString()
	{
		return Formats.formatDegrees(deg());
	}
}
