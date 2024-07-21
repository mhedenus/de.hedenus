package de.hedenus.astro.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.hedenus.astro.AstroException;
import de.hedenus.astro.Constellation;
import de.hedenus.astro.geom.Declination;
import de.hedenus.astro.geom.EquatorialCoordinates;
import de.hedenus.astro.geom.RightAscension;

public class StarCatalogue implements Serializable
{
	private static final long serialVersionUID = -3962841474181308467L;

	public static final class Entry implements Serializable
	{
		private static final long serialVersionUID = 2876671165655900332L;

		private final Integer nr;
		private final String flamsteedDesignation;
		private final String bayerDesignation;
		private final String properName;
		private final EquatorialCoordinates coordinates;
		private final Float apparentMagnitude;
		private final Constellation constellation;

		public Entry(final Integer nr, //
				final String flamsteedDesignation, //
				final String bayerDesignation, //
				final String properName, //
				final EquatorialCoordinates coordinates, //
				final Float apparentMagnitude, //
				final Constellation constellation)
		{
			this.nr = nr;
			this.flamsteedDesignation = flamsteedDesignation;
			this.bayerDesignation = bayerDesignation;
			this.properName = properName;
			this.coordinates = coordinates;
			this.apparentMagnitude = apparentMagnitude;
			this.constellation = constellation;
		}

		@Override
		public String toString()
		{
			return nr + "|" + name() + "|" + coordinates + "|" + apparentMagnitude;
		}

		public String flamsteedDesignation()
		{
			return flamsteedDesignation;
		}

		public String bayerDesignation()
		{
			return bayerDesignation;
		}

		public String properName()
		{
			return properName;
		}

		public boolean isStar()
		{
			return coordinates != null && apparentMagnitude != null;
		}

		public EquatorialCoordinates coordinates()
		{
			return coordinates;
		}

		public Float apparentMagnitude()
		{
			return apparentMagnitude;
		}

		public Constellation constellation()
		{
			return constellation;
		}

		public String name()
		{
			String result = properName;
			if (result == null)
			{
				result = bayerDesignation;
			}
			if (result == null)
			{
				result = flamsteedDesignation;
			}
			if (result == null)
			{
				result = "HR" + nr;
			}
			return result;
		}
	}

	private final List<Entry> entries = new ArrayList<>();

	public StarCatalogue()
	{
		load();
	}

	private void load()
	{
		Map<Integer, String> properNames = new HashMap<>();
		try (InputStream inputStream = getClass().getResourceAsStream("/bsc/starnames_de.dat"))
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			for (String line = null; (line = br.readLine()) != null;)
			{
				String s = line.trim();
				if (!s.isEmpty())
				{
					String[] tokens = line.split(";");
					Integer nr = Integer.valueOf(tokens[0]);
					String properName = tokens[tokens.length - 1];
					properNames.put(nr, properName);
				}
			}

		}
		catch (IOException ex)
		{
			throw new AstroException(ex);
		}

		try (InputStream inputStream = getClass().getResourceAsStream("/bsc/catalog.dat"))
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			for (String line = null; (line = br.readLine()) != null;)
			{
				String s = line.trim();
				if (!s.isEmpty())
				{
					Integer nr = Integer.parseInt(line.substring(0, 4).trim());
					String name = line.substring(4, 14);

					String flamsteedDesignation = null;
					String bayerDesignation = null;
					String properName = properNames.get(nr);

					EquatorialCoordinates coords = null;
					Float magnitude = null;
					Constellation constellation = null;

					if (!(name.contains("NOVA") || //
							nr.equals(95) || //
							nr.equals(182) || //
							nr.equals(2496) || //
							nr.equals(3515) || //
							nr.equals(3671))) //
					{
						String flnr = name.substring(0, 3).trim();
						String bynr = name.substring(3, 6).trim();
						String byi = name.substring(6, 7).trim();

						if (flnr.length() > 0)
						{
							flamsteedDesignation = flnr;
						}
						if (bynr.length() > 0)
						{
							bayerDesignation = toGreek(bynr);
							if (byi.length() > 0)
							{
								bayerDesignation += toSuperScript(byi);
							}

							String con = name.substring(7, 10);
							constellation = Constellation.valueOf(con);
						}

						String ra_h = line.substring(75, 77);
						String ra_m = line.substring(77, 79);
						String ra_s = line.substring(79, 83);

						String dec_sign = line.substring(83, 84);
						String dec_h = line.substring(84, 86);
						String dec_m = line.substring(86, 88);
						String dec_s = line.substring(88, 90);

						String vmag = line.substring(102, 107);

						RightAscension ra = new RightAscension(Integer.parseInt(ra_h), Integer.parseInt(ra_m),
								Float.parseFloat(ra_s));

						Declination dec = new Declination("-".equals(dec_sign) ? -1 : 1, Integer.parseInt(dec_h),
								Integer.parseInt(dec_m), Float.parseFloat(dec_s));

						coords = new EquatorialCoordinates(ra, dec);
						magnitude = Float.valueOf(vmag.trim());
					}

					Entry entry = new Entry(nr, flamsteedDesignation, bayerDesignation, properName, coords, magnitude,
							constellation);
					entries.add(entry);
				}
			}
		}
		catch (IOException ex)
		{
			throw new AstroException(ex);
		}

	}

	public List<Entry> entries()
	{
		return entries;
	}

	public Entry entry(final Integer nr)
	{
		return entries.get(nr.intValue() - 1);
	}

	public Stream<Entry> stars(final float magnitude)
	{
		return entries.stream().filter(Entry::isStar).filter(s -> s.apparentMagnitude() <= magnitude);
	}

	public static String toGreek(final String s)
	{
		return switch (s)
		{
			case "Alp" -> "\u03b1"; //
			case "Bet" -> "\u03b2"; //
			case "Gam" -> "\u03b3"; //
			case "Del" -> "\u03b4"; //
			case "Eps" -> "\u03b5"; //
			case "Zet" -> "\u03b6"; //
			case "Eta" -> "\u03b7"; //
			case "The" -> "\u03b8"; //
			case "Iot" -> "\u03b9"; //
			case "Kap" -> "\u03ba"; //
			case "Lam" -> "\u03bb"; //
			case "Mu" -> "\u03bc"; //
			case "Nu" -> "\u03bd"; //
			case "Xi" -> "\u03be"; //
			case "Omi" -> "\u03bf"; //
			case "Pi" -> "\u03c0"; //
			case "Rho" -> "\u03c1"; //
			case "Sig" -> "\u03c3"; // c2 == Abschluss-Sigma
			case "Tau" -> "\u03c4"; //
			case "Ups" -> "\u03c5"; //
			case "Phi" -> "\u03c6"; //
			case "Chi" -> "\u03c7"; //
			case "Psi" -> "\u03c8"; //
			case "Ome" -> "\u03c9"; //

			default -> throw new IllegalStateException(s);
		};

	}

	public static String toSuperScript(final String s)
	{
		return switch (s)
		{
			case "1" -> "\u00b9"; //
			case "2" -> "²"; //
			case "3" -> "³"; //
			case "4" -> "\u2074"; //
			case "5" -> "\u2075"; //
			case "6" -> "\u2076"; //
			case "7" -> "\u2077"; //
			case "8" -> "\u2078"; //
			case "9" -> "\u2079"; //
			default -> throw new IllegalStateException(s);
		};
	}
}
