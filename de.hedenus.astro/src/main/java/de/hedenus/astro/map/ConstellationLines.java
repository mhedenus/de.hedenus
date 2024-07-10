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

import de.hedenus.astro.AstroException;
import de.hedenus.astro.Constellation;

public class ConstellationLines implements Serializable
{
	private static final long serialVersionUID = 1780615707853865283L;

	private final StarCatalogue starCatalogue;
	private final Map<Constellation, List<SphericalLine>> lines = new HashMap<>();

	public ConstellationLines(final StarCatalogue starCatalogue)
	{
		this.starCatalogue = starCatalogue;
		load();
	}

	private void load()
	{
		try (InputStream inputStream = getClass().getResourceAsStream("/bsc/ConstellationLines.dat"))
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			for (String line = null; (line = br.readLine()) != null;)
			{
				String s = line.trim();
				if (!(s.isEmpty() || s.startsWith("#")))
				{
					String[] tokens = s.split("\\s+");

					Constellation constellation = Constellation.valueOf(tokens[0]);
					List<SphericalLine> segment = new ArrayList<>();

					int count = Integer.parseInt(tokens[1]);
					for (int i = 0; i < count - 1; i++)
					{
						int nr0 = Integer.parseInt(tokens[2 + i + 0]);
						int nr1 = Integer.parseInt(tokens[2 + i + 1]);
						segment.add(new SphericalLine(starCatalogue.entry(nr0).coordinates(),
								starCatalogue.entry(nr1).coordinates()));
					}

					lines.put(constellation, segment);
				}
			}
		}
		catch (IOException ex)
		{
			throw new AstroException(ex);
		}
	}

	public List<SphericalLine> lines(final Constellation constellation)
	{
		return lines.get(constellation);
	}
}
