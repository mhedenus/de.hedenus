package de.hedenus.astro.time;

import java.io.Serializable;

public class JulianDate implements Serializable
{
	private static final long serialVersionUID = 1751589575253638324L;

	private final double jd;

	public JulianDate(final int year, final int month, final int day, final int hour, final int min, final int sec)
	{
		this(computeJD(year, month, day, hour, min, sec));
	}

	public JulianDate(final double jd)
	{
		this.jd = jd;
	}

	public double jd()
	{
		return jd;
	}

	@Override
	public String toString()
	{
		return String.valueOf(jd());
	}

	static double computeJD(final int year, final int month, final int day, final int hour, final int min,
			final int sec)
	{
		int a = (14 - month) / 12;
		int y = year + 4800 - a;
		int m = month + (12 * a) - 3;
		int jdn = day + ((153 * m + 2) / 5) + (365 * y) + (y / 4) - (y / 100) + (y / 400) - 32045;

		double jd = jdn + ((hour - 12) / 24.0) + (min / 1440.0) + (sec / 86400.0);
		return jd;
	}
}
