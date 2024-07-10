package de.hedenus.astro.geom;

import de.hedenus.astro.common.Formats;

public class RightAscension extends Angle
{
	private static final long serialVersionUID = 2065307103495267857L;

	private final int h;
	private final int m;
	private final float s;

	public static RightAscension fromDecimalDegree(double val)
	{
		val = Angle.fold360(val);

		double t = val * (24 * 60 * 60 / 360);

		int H = (int) Math.floor(t / (60.0 * 60.0));
		int h = (H % 24);
		t = t - (H * 60.0 * 60.0);

		int m = (int) Math.floor(t / 60.0);
		t = t - (m * 60.0);

		float s = (float) t;

		return new RightAscension(h, m, s);
	}

	public static RightAscension fromString(final String string)
	{
		RightAscension result;
		double[] vals = Formats.parseTuple(string);
		switch (vals.length)
		{
		case 0:
			result = fromDecimalDegree(vals[0]);
			break;
		case 3:
			int h = Formats.checkInteger(vals[0], 0, 24);
			int m = Formats.checkInteger(vals[1], 0, 60);
			float s = Formats.checkFloat(vals[2], 0.0f, 60.0f);
			result = new RightAscension(h, m, s);

			break;
		default:
			throw new NumberFormatException("one or three tokens expected");

		}
		return result;
	}

	public RightAscension(final int h, final int m, final float s)
	{
		super((h + (m / 60.0) + (s / 3600.0)) * 15.0);
		this.h = h;
		this.m = m;
		this.s = s;
	}

	@Override
	public String toString()
	{
		return Formats.twoDigits(h) + "h " + Formats.twoDigits(m) + "m " + Formats.formatSeconds(s) + "s";
	}
}
