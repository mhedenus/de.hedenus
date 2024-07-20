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
import de.hedenus.astro.geom.SphericalCoordinates;

public class ConstellationBoundaries implements Serializable
{
	private static final long serialVersionUID = 3960679160825662652L;

	private static Map<Constellation, ConstellationBoundaries> constellationBoundaries = new HashMap<Constellation, ConstellationBoundaries>();

	public static ConstellationBoundaries constellationBoundaries(final Constellation constellation)
	{
		ConstellationBoundaries result = constellationBoundaries.get(constellation);
		if (result == null)
		{
			result = FileCache.instance().get(constellation.name(),
					() -> new ConstellationBoundaries(constellation).downloadIAUData());
			constellationBoundaries.put(constellation, result);
		}
		return result;
	}

	private final Constellation constellation;
	private final List<SphericalCoordinates> points = new ArrayList<>();

	public ConstellationBoundaries(final Constellation constellation)
	{
		this.constellation = constellation;
	}

	private ConstellationBoundaries downloadIAUData()
	{
		try
		{
			URL url = constellation == Constellation.Oct ? getClass().getResource("/iau/oct-patched.txt")
					: URI.create("https://www.iau.org/static/public/constellations/txt/"
							+ constellation.name().toLowerCase() + ".txt").toURL();

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

	/*
	public static Map<Constellation, ConstellationBoundaries> loadBarbierData()
	{
		Map<Constellation, ConstellationBoundaries> result = new HashMap<>();
	
		Map<String, Constellation> constuc = Stream.of(Constellation.values())
				.collect(Collectors.toMap(c -> c.name().toUpperCase(), c -> c));
	
		try (InputStream inputStream = ConstellationBoundaries.class.getResourceAsStream("/pbarbier/bound_in_20.txt"))
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
	
			for (String line = null; (line = br.readLine()) != null;)
			{
				String s = line.trim();
				if (!s.isEmpty())
				{
					String[] tokens = s.split("\\s+");
					Constellation constellation = constuc.get(tokens[2]);
					ConstellationBoundaries constellationBoundaries = result.get(constellation);
					if (constellationBoundaries == null)
					{
						constellationBoundaries = new ConstellationBoundaries(constellation);
						result.put(constellation, constellationBoundaries);
					}
					RightAscension ra = RightAscension.fromDecimalDegree(360.0 * Double.valueOf(tokens[0]) / 24.0);
					Declination dec = Declination.fromDecimalDegree(Double.valueOf(tokens[1]));
					constellationBoundaries.points().add(new EquatorialCoordinates(ra, dec));
				}
			}
		}
		catch (IOException ex)
		{
			throw new AstroException(ex);
		}
		return result;
	}
	*/

	public List<SphericalCoordinates> points()
	{
		return points;
	}

	public List<SphericalLine> lines()
	{
		List<SphericalLine> lines = new ArrayList<SphericalLine>();
		for (int i = 0; i < points.size(); i++)
		{
			SphericalCoordinates x1 = points.get(i + 0);
			SphericalCoordinates x2 = points.get((i + 1) % points.size());
			lines.add(new SphericalLine(x1, x2));
		}
		return lines;
	}

	public void sketch()
	{
		int margin = 20;
		int w = 720 + 2 * margin;
		int h = 360 + 2 * margin;

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2d = bi.createGraphics();
		graphics2d.setColor(Color.white);
		graphics2d.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		graphics2d.setColor(Color.red);

		for (SphericalLine line : lines())
		{
			int x0 = margin + (720 - 2 * (int) line.x0().x().deg());
			int y0 = margin + (180 - 2 * (int) line.x0().y().deg());

			int x1 = margin + (720 - 2 * (int) line.x1().x().deg());
			int y1 = margin + (180 - 2 * (int) line.x1().y().deg());

			graphics2d.drawLine(x0, y0, x1, y1);
			graphics2d.fillOval(x0 - 2, y0 - 2, 4, 4);
			graphics2d.fillOval(x1 - 2, y1 - 2, 4, 4);
		}

		graphics2d.setColor(Color.black);
		for (int i = 0; i < points.size(); i++)
		{
			SphericalCoordinates x1 = points.get(i);
			int x = margin + (720 - 2 * (int) x1.x().deg());
			int y = margin + (180 - 2 * (int) x1.y().deg());

			graphics2d.drawString(String.valueOf(i + 1), x, y);
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
