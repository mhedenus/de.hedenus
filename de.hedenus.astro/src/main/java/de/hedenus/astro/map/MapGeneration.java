package de.hedenus.astro.map;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hedenus.astro.AstroException;
import de.hedenus.astro.Constellation;
import de.hedenus.astro.common.FileCache;
import de.hedenus.astro.common.Log;
import de.hedenus.astro.geom.DecimalDegree;
import de.hedenus.astro.geom.SphericalCoordinates;
import de.hedenus.astro.map.StarCatalogue.Entry;

public class MapGeneration
{
	public static void main(final String[] args)
	{
		long t0 = System.currentTimeMillis();

		new MapGeneration(8000).draw().save();

		Log.info("Done in " + ((System.currentTimeMillis() - t0) / 1000.0f) + "s");
	}

	private final Settings settings;
	private final StarMap map;
	private final Graphics2D g2d;
	private final MapProjection mapProjection;
	private final StarCatalogue starCatalogue;
	private final ConstellationLines constellationLines;
	private final ConstellationCenters constellationCenters;
	private final StarLabels starLabels;

	public MapGeneration(final int dim)
	{
		this.settings = Settings.defaultSettings(dim);
		this.map = new StarMap(settings);
		this.g2d = map.graphics2d();
		this.mapProjection = new MapProjection(settings.size);

		this.starCatalogue = new StarCatalogue();
		this.constellationLines = FileCache.instance().get("constellationLines",
				() -> new ConstellationLines().compute(starCatalogue));
		this.constellationCenters = new ConstellationCenters();

		this.starLabels = new StarLabels(settings);
	}

	public MapGeneration draw()
	{
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		drawRaster();
		drawConstellationBoundaries();
		drawEcliptic();

		drawConstellationLines();
		drawStars();
		drawStarLabels();
		drawConstellationLabels();

		return this;
	}

	public void animate()
	{
		for (int a = 0; a < 360; a++)
		{
			mapProjection.rotation(a);
			draw();
			//save("anim/animation_" + a + ".png");
		}
	}

	public MapGeneration drawRaster()
	{
		g2d.setColor(settings.backgroundColor);
		g2d.fill(new Ellipse2D.Double(0, 0, settings.size.width, settings.size.height));

		g2d.setColor(settings.rasterColor);
		g2d.setStroke(new BasicStroke(settings.rasterLineWidth));

		int d = 15;

		// ra
		int x2 = settings.size.width / 2;
		g2d.drawLine(x2, 0, x2, settings.size.height);

		for (int i = d; i <= 180; i += d)
		{
			int w2 = i * settings.size.width / 360;
			g2d.drawOval(x2 - w2, 0, 2 * w2, settings.size.height);
		}

		//dec
		g2d.clip(new Ellipse2D.Double(0, 0, settings.size.width, settings.size.height));
		for (int i = -90 + d; i < 90; i += d)
		{
			int y = mapProjection.project(new SphericalCoordinates(new DecimalDegree(0), new DecimalDegree(i))).y;
			g2d.drawLine(0, y, settings.size.width, y);
		}

		//// labels

		g2d.setFont(new Font(settings.rasterLabelFontName, Font.PLAIN, settings.rasterLabelFontSize));
		FontMetrics fontMetrics = g2d.getFontMetrics();

		// ra
		for (int i = d; i < 180; i += d)
		{
			String ra = (24 - (i * 24 / 360)) + "h";
			Rectangle bounds = fontMetrics.getStringBounds(ra, g2d).getBounds();

			int x = (settings.size.width / 2) + (i * settings.size.width / 360) + settings.rasterLabelGap;
			int y = (settings.size.height / 2) - bounds.y;
			g2d.drawString(ra, x, y);
		}
		for (int i = d; i < 180; i += d)
		{
			String ra = (i * 24 / 360) + "h";
			Rectangle bounds = fontMetrics.getStringBounds(ra, g2d).getBounds();

			int x = (settings.size.width / 2) - (i * settings.size.width / 360) - bounds.width
					- settings.rasterLabelGap;
			int y = (settings.size.height / 2) - bounds.y;
			g2d.drawString(ra, x, y);
		}

		// dec
		for (int i = -90 + d; i < 90; i += d)
		{
			String dec = i == 0 ? "\u03b3" : i + "°";
			//Rectangle bounds = fontMetrics.getStringBounds(dec, g2d).getBounds();
			int x = (settings.size.width / 2) + settings.rasterLabelGap;
			int y = mapProjection.project(new SphericalCoordinates(new DecimalDegree(0), new DecimalDegree(i))).y
					- settings.rasterLabelGap;
			g2d.drawString(dec, x, y);
		}

		return this;
	}

	public MapGeneration drawEcliptic()
	{
		g2d.setColor(settings.eclipticColor);
		g2d.setStroke(new BasicStroke(settings.eclipticLineWidth));

		double step = 360.0 / settings.dim / 10.0;
		SphericalCoordinates p0 = computeEclipticPoint(-180.0);
		Point px0 = mapProjection.project(p0);
		for (double lam = -180.0 + step; lam <= 180.0; lam = lam + step)
		{
			SphericalCoordinates p1 = computeEclipticPoint(lam);
			Point px1 = mapProjection.project(p1);
			g2d.drawLine(px0.x, px0.y, px1.x, px1.y);
			px0 = px1;
		}

		return this;
	}

	// eps(J2000) = 23° 26′ 21,45″
	private static final double eps = 23.0 + (26.0 / 60.0) + (21.45 / 3600.0);
	private static final double sin_eps = Math.sin(Math.toRadians(eps));
	private static final double cos_eps = Math.cos(Math.toRadians(eps));

	private SphericalCoordinates computeEclipticPoint(final double lam)
	{
		double l = Math.toRadians(lam);

		// tan ra = sin lam * cos eps / cos lam
		// sin dec =  sin lam * sin eps
		double ra = Math.toDegrees(Math.atan2(Math.sin(l) * cos_eps, Math.cos(l)));
		double dec = Math.toDegrees(Math.asin(Math.sin(l) * sin_eps));

		return new SphericalCoordinates(new DecimalDegree(ra), new DecimalDegree(dec));
	}

	public MapGeneration drawConstellationBoundaries()
	{
		g2d.setColor(settings.constellationBoundariesColor);
		g2d.setStroke(new BasicStroke(settings.rasterLineWidth));

		Set<SphericalLine> linesDuplicateFilter = new HashSet<>();

		for (Constellation constellation : Constellation.values())
		{
			if (constellation != Constellation.Ser)
			{
				ConstellationBoundaries constellationBoundaries = ConstellationBoundaries
						.constellationBoundaries(constellation);

				for (SphericalLine sphericalLine : constellationBoundaries.lines())
				{
					if (linesDuplicateFilter.add(sphericalLine))
					{
						List<Line2D> line2Ds = mapProjection.project(sphericalLine);
						for (Line2D line2D : line2Ds)
						{
							g2d.drawLine(line2D.p0.x, line2D.p0.y, line2D.p1.x, line2D.p1.y);
						}
					}
				}
			}
		}
		return this;
	}

	public MapGeneration drawConstellationLabels()
	{
		g2d.setColor(settings.constellationLabelColor);
		g2d.setFont(new Font(settings.constellationLabelFontName, Font.BOLD, settings.constellationLabelFontSize));
		FontMetrics fontMetrics = g2d.getFontMetrics();

		for (Constellation constellation : Constellation.values())
		{
			if (constellation != Constellation.Ser)
			{
				Point px = mapProjection.project(constellationCenters.center(constellation));

				String name = constellation.label();

				Rectangle bounds = fontMetrics.getStringBounds(name, g2d).getBounds();

				g2d.drawString(name, px.x - bounds.width / 2, px.y + bounds.y + bounds.height / 2);
			}
		}
		return this;
	}

	public MapGeneration drawConstellationLines()
	{
		g2d.setColor(settings.constellationLineColor);
		g2d.setStroke(new BasicStroke(settings.constellationBoundariesLineWidth));

		for (Constellation constellation : Constellation.values())
		{
			if (constellation != Constellation.Ser)
			{
				List<SphericalLine> sphericalLines = constellationLines.lines(constellation);
				if (sphericalLines != null)
				{
					for (SphericalLine sphericalLine : sphericalLines)
					{
						List<Line2D> line2Ds = mapProjection.project(sphericalLine);
						for (Line2D line2D : line2Ds)
						{
							g2d.drawLine(line2D.p0.x, line2D.p0.y, line2D.p1.x, line2D.p1.y);
						}
					}
				}
			}
		}
		return this;
	}

	public MapGeneration drawStars()
	{
		g2d.setColor(settings.starColor);
		g2d.setFont(new Font(settings.starLabelFontName, Font.PLAIN, settings.starLabelFontSize));
		FontMetrics fontMetrics = g2d.getFontMetrics();

		List<StarCatalogue.Entry> starsWithProperName = new ArrayList<>();

		starCatalogue.stars(settings.starMaxMagnitude).forEach(entry -> {
			Point px = mapProjection.project(entry.coordinates());

			int starWidth = Math.round(settings.starScale * (8.0f - entry.apparentMagnitude()));
			int starRadius = starWidth / 2;

			boolean highlight = false;

			String bayerDesignation = entry.bayerDesignation();
			if (bayerDesignation != null)
			{
				Rectangle bounds = fontMetrics.getStringBounds(bayerDesignation, g2d).getBounds();
				starLabels.add(new StarLabel(settings, bayerDesignation, px, starRadius, bounds));

				String properName = entry.properName();
				if (properName != null)
				{
					starsWithProperName.add(entry);
					highlight = true;
				}
			}

			if (highlight)
			{
				Path2D.Double path = new Path2D.Double();
				double phi = 2.0 * Math.PI / 10.0;
				double r2 = 2.0 * starRadius;
				double r1 = r2 / 2.618034;
				path.moveTo(px.x - r2 * Math.sin(0 * phi), px.y - r2 * Math.cos(0 * phi));
				path.lineTo(px.x - r1 * Math.sin(1 * phi), px.y - r1 * Math.cos(1 * phi));
				path.lineTo(px.x - r2 * Math.sin(2 * phi), px.y - r2 * Math.cos(2 * phi));
				path.lineTo(px.x - r1 * Math.sin(3 * phi), px.y - r1 * Math.cos(3 * phi));
				path.lineTo(px.x - r2 * Math.sin(4 * phi), px.y - r2 * Math.cos(4 * phi));
				path.lineTo(px.x - r1 * Math.sin(5 * phi), px.y - r1 * Math.cos(5 * phi));
				path.lineTo(px.x - r2 * Math.sin(6 * phi), px.y - r2 * Math.cos(6 * phi));
				path.lineTo(px.x - r1 * Math.sin(7 * phi), px.y - r1 * Math.cos(7 * phi));
				path.lineTo(px.x - r2 * Math.sin(8 * phi), px.y - r2 * Math.cos(8 * phi));
				path.lineTo(px.x - r1 * Math.sin(9 * phi), px.y - r1 * Math.cos(9 * phi));
				path.closePath();
				g2d.fill(path);
			}
			else
			{
				g2d.fillOval(px.x - starRadius, px.y - starRadius, starWidth, starWidth);
			}
		});

		starsWithProperName.sort(new Comparator<StarCatalogue.Entry>()
		{
			@Override
			public int compare(final Entry e1, final Entry e2)
			{
				int r = e1.constellation().compareTo(e2.constellation());
				if (r == 0)
				{
					char letter1 = e1.bayerDesignation().charAt(0);
					char letter2 = e2.bayerDesignation().charAt(0);
					r = letter1 - letter2;
					if (r == 0)
					{
						int i1 = index(e1);
						int i2 = index(e1);
						r = i1 - i2;
					}
				}
				return r;
			}

			private int index(final Entry e)
			{
				int i = 0;
				if (e.bayerDesignation().length() > 1)
				{
					i = switch (e.bayerDesignation().charAt(1))
					{
						case '\u00b9' -> 1;
						case '²' -> 2;
						case '³' -> 3;
						case '\u2074' -> 4;
						case '\u2075' -> 5;
						case '\u2076' -> 6;
						case '\u2077' -> 7;
						case '\u2078' -> 8;
						case '\u2079' -> 9;
						default -> throw new IllegalStateException();
					};
				}
				return i;
			}
		});

		try (PrintWriter pw = new PrintWriter(
				new FileWriter(new File("target/starsWithProperNames.csv"), StandardCharsets.UTF_8)))
		{
			for (StarCatalogue.Entry e : starsWithProperName)
			{
				pw.println(e.HR_number() + ";" + e.bayerDesignation() + ";" + e.constellation() + ";" + e.properName());
			}
		}
		catch (IOException ex)
		{
			throw new AstroException(ex);
		}

		return this;
	}

	public MapGeneration drawStarLabels()
	{
		g2d.setColor(settings.starLabelColor);

		starLabels.layout();

		for (StarLabel label : starLabels)
		{
			label.draw(g2d);
		}

		return this;
	}

	public MapGeneration save()
	{
		map.save(new File("target", "starMap_" + settings.dim + ".png"));
		return this;
	}
}
