package de.hedenus.astro.common;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

public class Formats
{
	public static final DecimalFormat FLOAT_NUMBER = new DecimalFormat("0.000", new DecimalFormatSymbols(Locale.US));

	public static String toFloatNumber(final double number)
	{
		return FLOAT_NUMBER.format(number);
	}

	public static final DecimalFormat ZAHL = new DecimalFormat("0.000", new DecimalFormatSymbols(Locale.GERMAN));

	public static String toZahl(final double number)
	{
		return ZAHL.format(number);
	}

	public static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	public static String currentDate()
	{
		return DATE.format(new Date());
	}

	private static DatatypeFactory xmlDatatypeFactory;

	static
	{
		try
		{
			xmlDatatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e)
		{
			throw new Error(e);
		}
	}

	public static long dateTimeAsSystemMillis(final String dateTime)
	{
		return xmlDatatypeFactory.newXMLGregorianCalendar(dateTime).toGregorianCalendar().getTimeInMillis();
	}

	private static final DecimalFormat DF = new DecimalFormat("0.000000°", new DecimalFormatSymbols(Locale.US));

	public static String formatDegrees(final double deg)
	{
		return DF.format(deg);
	}

	public static String twoDigits(final int v)
	{
		return v < 10 ? "0" + v : Integer.toString(v);
	}

	private static final DecimalFormat SF = new DecimalFormat("00.0000", new DecimalFormatSymbols(Locale.US));

	public static String formatSeconds(final float s)
	{
		return SF.format(s);
	}

	public static int checkInteger(final double val, final int minInclusive, final int maxExclusive)
	{
		int ival = (int) val;
		if (ival != val)
		{
			throw new NumberFormatException("integer value expected");
		}

		if (ival < minInclusive)
		{
			throw new IllegalArgumentException("too small:" + val);
		}

		if (ival >= maxExclusive)
		{
			throw new IllegalArgumentException("too big:" + val);
		}

		return ival;
	}

	public static float checkFloat(final double val, final float minInclusive, final float maxExclusive)
	{
		float fval = (float) val;

		if (fval < minInclusive)
		{
			throw new IllegalArgumentException("too small:" + val);
		}

		if (fval >= maxExclusive)
		{
			throw new IllegalArgumentException("too big:" + val);
		}

		return fval;
	}

	public static double[] parseTuple(final String s)
	{
		String[] tokens = s.split("[^0-9\\-\\,\\.]+");
		double[] vals = new double[tokens.length];

		int v = 0;
		for (int i = 0; i < tokens.length; i++)
		{
			if (!tokens[i].isEmpty())
			{
				vals[v++] = Double.parseDouble(tokens[i].replace(',', '.'));
			}
		}
		return Arrays.copyOf(vals, v);
	}

	private Formats()
	{
		//NOP
	}
}
