package de.hedenus.astro.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hedenus.astro.AstroException;
import de.hedenus.astro.Constellation;
import de.hedenus.astro.common.FileCache;
import de.hedenus.astro.common.Log;
import de.hedenus.astro.geom.Declination;
import de.hedenus.astro.geom.EquatorialCoordinates;
import de.hedenus.astro.geom.RightAscension;

public class ConstellationBoundaries implements Serializable
{
	private static final long serialVersionUID = 3960679160825662652L;

	public static ConstellationBoundaries boundaries(final Constellation constellation)
	{
		ConstellationBoundaries constellationBoundaries = FileCache.instance().get(constellation.name(),
				() -> new ConstellationBoundaries(constellation).download());
		return constellationBoundaries;
	}

	private final Constellation constellation;
	private final List<EquatorialCoordinates> points = new ArrayList<>();

	private ConstellationBoundaries(final Constellation constellation)
	{
		this.constellation = constellation;
	}

	private ConstellationBoundaries download()
	{
		try
		{
			URL url = url();
			Log.info("Downloading: " + url.toExternalForm());

			try (InputStream inputStream = url.openStream())
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

				for (String line = null; (line = br.readLine()) != null;)
				{
					String s = line.trim();
					if (!s.isEmpty())
					{
						String[] parts = s.split("\\|");

						RightAscension ra = RightAscension.fromString(parts[0].trim());
						Declination dec = Declination.fromString(parts[1].trim());

						this.points.add(new EquatorialCoordinates(ra, dec));
					}
				}
			}
		}
		catch (IOException ex)
		{
			throw new AstroException(ex);
		}
		return this;
	}

	public List<EquatorialCoordinates> points()
	{
		return points;
	}

	public List<SphericalLine> lines()
	{
		List<SphericalLine> lines = new ArrayList<SphericalLine>();
		for (int i = 0; i < points.size(); i++)
		{
			EquatorialCoordinates x1 = points.get(i + 0);
			EquatorialCoordinates x2 = points.get((i + 1) % points.size());
			lines.add(new SphericalLine(x1, x2));
		}
		return lines;
	}

	private URL url() throws IOException
	{
		return URI.create(
				"https://www.iau.org/static/public/constellations/txt/" + constellation.name().toLowerCase() + ".txt")
				.toURL();
	}
}
