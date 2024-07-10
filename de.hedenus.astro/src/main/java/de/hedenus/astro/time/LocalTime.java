package de.hedenus.astro.time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import de.hedenus.astro.common.Defaults;
import de.hedenus.astro.common.Formats;
import de.hedenus.astro.geom.Location;

public class LocalTime implements Serializable
{
	private static final long serialVersionUID = 5488478363152256774L;

	public final Location location;
	public final int year;
	public final int month;
	public final int day;
	public final int hour;
	public final int minute;
	public final int second;
	public final String weekday;
	public final SiderealTime siderealTime;

	public final double jd0h;

	public LocalTime()
	{
		this(Defaults.LOCATION);
	}

	public LocalTime(final Location location)
	{
		this.location = location;

		GregorianCalendar local = new GregorianCalendar();
		year = local.get(Calendar.YEAR);
		month = local.get(Calendar.MONTH) + 1;
		day = local.get(Calendar.DAY_OF_MONTH);
		hour = local.get(Calendar.HOUR_OF_DAY);
		minute = local.get(Calendar.MINUTE);
		second = local.get(Calendar.SECOND);
		weekday = local.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.GERMAN);

		GregorianCalendar utc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		int yearUTC = utc.get(Calendar.YEAR);
		int monthUTC = utc.get(Calendar.MONTH) + 1;
		int dayUTC = utc.get(Calendar.DAY_OF_MONTH);
		int hourUTC = utc.get(Calendar.HOUR_OF_DAY);
		int minuteUTC = utc.get(Calendar.MINUTE);
		int secondUTC = utc.get(Calendar.SECOND);

		JulianDate today0h = new JulianDate(yearUTC, monthUTC, dayUTC, 0, 0, 0);
		jd0h = today0h.jd();

		double secondsUTC = (60 * 60 * hourUTC) + (60 * minuteUTC) + secondUTC;
		siderealTime = new SiderealTime(today0h, secondsUTC, location.getCoordinates().getLongitude().deg());
	}

	@Override
	public String toString()
	{
		return location + "; " + weekday + ", " + Formats.twoDigits(day) + "." + Formats.twoDigits(month) + "." + year
				+ " " + Formats.twoDigits(hour) + ":" + Formats.twoDigits(minute) + ":" + Formats.twoDigits(second) //
				+ "; Sternzeit " + siderealTime;
	}
}
