package de.hedenus.astro.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

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

	private static Map<Constellation, ConstellationBoundaries> constellationBoundaries = new HashMap<Constellation, ConstellationBoundaries>();

	public static ConstellationBoundaries boundaries(final Constellation constellation)
	{
		ConstellationBoundaries result = constellationBoundaries.get(constellation);
		if (result == null)
		{
			result = FileCache.instance().get(constellation.name(),
					() -> new ConstellationBoundaries(constellation).download());
			constellationBoundaries.put(constellation, result);
		}
		return result;
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

	public void sketch()
	{
		int w = 720;
		int h = 360;

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2d = bi.createGraphics();
		graphics2d.setColor(Color.white);
		graphics2d.fillRect(0, 0, w, h);
		graphics2d.setColor(Color.red);

		for (SphericalLine line : lines())
		{
			int x0 = 720 - 2 * (int) line.x0().x().deg();
			int y0 = 180 - 2 * (int) line.x0().y().deg();

			int x1 = 720 - 2 * (int) line.x1().x().deg();
			int y1 = 180 - 2 * (int) line.x1().y().deg();

			graphics2d.drawLine(x0, y0, x1, y1);
			graphics2d.fillOval(x0 - 2, y0 - 2, 4, 4);
			graphics2d.fillOval(x1 - 2, y1 - 2, 4, 4);
		}

		try
		{
			ImageIO.write(bi, "PNG", new File("target", constellation.name() + ".png"));
		}
		catch (IOException ex)
		{
			throw new AstroException(ex);
		}
	}
}
