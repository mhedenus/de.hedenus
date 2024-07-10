package de.hedenus.astro.geom;

import de.hedenus.astro.common.Formats;

public class Declination extends Angle
{
	private static final long serialVersionUID = 5469517853958231534L;

	public static Declination fromDecimalDegree(final double val)
	{
		if (val < -90.0 || val > 90.0)
		{
			throw new IllegalArgumentException("value in interval [-90�,+90�] expected");
		}

		int sign = val < 0.0 ? -1 : 1;
		double t = Math.abs(val) * 60.0 * 60.0;

		int deg = (int) Math.floor(t / (60.0 * 60.0));
		t = t - (deg * 60.0 * 60.0);

		int min = (int) Math.floor(t / 60.0);
		t = t - (min * 60.0);

		float sec = (float) t;

		return new Declination(sign, deg, min, sec);
	}

	public static Declination fromString(final String string)
	{
		Declination result;
		double[] vals = Formats.parseTuple(string);
		switch (vals.length)
		{
		case 1:
			result = fromDecimalDegree(vals[0]);
			break;
		case 3:
			int deg = Formats.checkInteger(vals[0], -90, 91);
			int min = Formats.checkInteger(vals[1], 0, 60);
			float sec = Formats.checkFloat(vals[2], 0.0f, 60.0f);
			result = new Declination(deg < 0 ? -1 : 1, Math.abs(deg), min, sec);

			break;
		default:
			throw new NumberFormatException("one or three tokens expected");

		}
		return result;
	}

	private final int sign;
	private final int deg;
	private final int min;
	private final float sec;

	public Declination(final int sign, final int deg, final int min, final float sec)
	{
		super(((double) sign) * (deg + (min / 60.0) + (sec / 3600.0)));
		this.sign = sign;
		this.deg = deg;
		this.min = min;
		this.sec = sec;
	}

	@Override
	public String toString()
	{
		return (sign < 0 ? "-" : "+") //
				+ digits(Math.abs(deg), 2) + "° " //
				+ digits(min, 2) + "' " //
				+ Formats.formatSeconds(sec) + "\"";
	}

	private String digits(final int val, final int d)
	{
		String s = String.valueOf(val);
		for (int i = d - s.length() - 1; i >= 0; i--)
		{
			s = "0" + s;
		}
		return s;
	}
}
