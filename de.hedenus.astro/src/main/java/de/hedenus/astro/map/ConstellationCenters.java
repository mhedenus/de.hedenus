package de.hedenus.astro.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hedenus.astro.AstroException;
import de.hedenus.astro.Constellation;
import de.hedenus.astro.geom.Declination;
import de.hedenus.astro.geom.EquatorialCoordinates;
import de.hedenus.astro.geom.RightAscension;

public class ConstellationCenters implements Serializable
{
	private static final long serialVersionUID = 3883031651060713896L;
	private final Map<Constellation, EquatorialCoordinates> centers = new HashMap<>();

	public ConstellationCenters()
	{
		load();
	}

	private void load()
	{
		Map<String, Constellation> constuc = Stream.of(Constellation.values())
				.collect(Collectors.toMap(c -> c.name().toUpperCase(), c -> c));

		try (InputStream inputStream = ConstellationBoundaries.class.getResourceAsStream("/pbarbier/centers_20.txt"))
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			for (String line = null; (line = br.readLine()) != null;)
			{
				String s = line.trim();
				if (!s.isEmpty())
				{
					String[] tokens = s.split("\\s+");
					Constellation constellation = constuc.get(tokens[4]);

					RightAscension ra = RightAscension.fromDecimalDegree(360.0 * Double.valueOf(tokens[0]) / 24.0);
					Declination dec = Declination.fromDecimalDegree(Double.valueOf(tokens[1]));
					centers.put(constellation, new EquatorialCoordinates(ra, dec));
				}
			}
		}
		catch (IOException ex)
		{
			throw new AstroException(ex);
		}
	}

	public EquatorialCoordinates center(final Constellation constellation)
	{
		return centers.get(constellation);
	}
}
