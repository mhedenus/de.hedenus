package de.hedenus.astro.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hedenus.astro.AstroException;
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
		private final String name;
		private final EquatorialCoordinates coordinates;
		private final Float apparentMagnitude;

		public Entry(final Integer nr, final String name, final EquatorialCoordinates coordinates,
				final Float apparentMagnitude)
		{
			this.nr = nr;
			this.name = name;
			this.coordinates = coordinates;
			this.apparentMagnitude = apparentMagnitude;
		}

		@Override
		public String toString()
		{
			return nr + "|" + name + "|" + coordinates + "|" + apparentMagnitude;
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
	}

	private final List<Entry> entries = new ArrayList<>();

	public StarCatalogue()
	{
		load();
	}

	private void load()
	{
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
					EquatorialCoordinates coords = null;
					Float magnitude = null;

					if (!(name.contains("NOVA") || //
							nr.equals(95) || //
							nr.equals(182) || //
							nr.equals(2496) || //
							nr.equals(3515) || //
							nr.equals(3671))) //
					{
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

					Entry entry = new Entry(nr, name, coords, magnitude);
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
}
