package de.hedenus.astro.map;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hedenus.astro.AstroException;
import de.hedenus.astro.Constellation;

public class ConstellationLines implements Serializable
{
	private static final long serialVersionUID = 1780615707853865283L;

	private final Map<Constellation, List<SphericalLine>> lines = new HashMap<>();

	public List<SphericalLine> lines(final Constellation constellation)
	{
		return lines.get(constellation);
	}

	public ConstellationLines compute(final StarCatalogue starCatalogue)
	{
		Map<Integer, Integer> hip2hr = new HashMap<Integer, Integer>();

		try (InputStream inputStream = getClass().getResourceAsStream("/stellarium/cross-id.dat"))
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			for (String line = null; (line = br.readLine()) != null;)
			{
				String s = line.trim();
				if (!(s.isEmpty() || s.startsWith("#")))
				{
					String[] tokens = s.split("\\s+");

					Integer hip = Integer.valueOf(tokens[0]);
					Integer hr = Integer.valueOf(tokens[tokens.length - 1]);
					hip2hr.put(hip, hr);
				}
			}
		}
		catch (Exception ex)
		{
			throw new AstroException(ex);
		}

		try (InputStream inputStream = getClass().getResourceAsStream("/stellarium/constellationship.fab"))
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			for (String line = null; (line = br.readLine()) != null;)
			{
				String s = line.trim();
				if (!(s.isEmpty() || s.startsWith("#")))
				{
					String[] tokens = s.split("\\s+");

					String con = tokens[0];
					Constellation constellation = "Ser".equals(con) ? Constellation.Ser1 : Constellation.valueOf(con);
					List<SphericalLine> linesOfConstellation = new ArrayList<SphericalLine>();

					int lineCount = Integer.parseInt(tokens[1]);
					for (int i = 0; i < lineCount; i++)
					{
						int hip1 = Integer.parseInt(tokens[2 + 2 * i + 0]);
						int hip2 = Integer.parseInt(tokens[2 + 2 * i + 1]);

						Integer hr1 = hip2hr.get(hip1);
						Integer hr2 = hip2hr.get(hip2);

						if (hr1 == null || hr2 == null)
						{
							System.err.println(con + " " + hip1 + "->" + hr1 + " " + hip2 + "->" + hr2);
						}
						else
						{
							linesOfConstellation.add(new SphericalLine(starCatalogue.entry(hr1).coordinates(),
									starCatalogue.entry(hr2).coordinates()));
						}
					}

					this.lines.put(constellation, linesOfConstellation);
				}
			}
		}
		catch (Exception ex)
		{
			throw new AstroException(ex);
		}

		return this;
	}
}
