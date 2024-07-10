package de.hedenus.astro.time;

import java.io.Serializable;

import de.hedenus.astro.common.Formats;

/**
 * See <a href="http://www.cv.nrao.edu/~rfisher/Ephemerides/times.html">Astronomical Times</a>.
 */
public class SiderealTime implements Serializable
{
	private static final long serialVersionUID = 7239635237995242110L;

	private final int h;
	private final int m;
	private final float s;

	public SiderealTime(final JulianDate today0h, final double secondsUTC, final double eastLongitude)
	{
		double jd = today0h.jd();
		double C = (jd - 2451545.0) / 36525.0;
		double GMST1 = 24110.54841 + (8640184.812866 * C) + (0.093104 * C * C) - (0.0000062 * C * C * C);

		// GAST = GMST + (equation of the equinoxes)
		// LST ~ LMST = GMST + (observer's east longitude)
		double GMST = GMST1 + (1.002737909 * secondsUTC);
		double LST = GMST + 240 * eastLongitude;

		double t = LST;

		int H = (int) (t / (60.0 * 60.0));
		h = (H % 24);
		t = t - (H * 60.0 * 60.0);

		m = (int) (t / 60.0);
		t = t - (m * 60.0);

		s = (float) t;
	}

	public double toDegrees()
	{
		return (h + (m / 60.0) + (s / 3600.0)) * 15.0;
	}

	@Override
	public String toString()
	{
		return Formats.twoDigits(h) + ":" + Formats.twoDigits(m) + ":" + Formats.twoDigits((int) s);
	}
}
