package de.hedenus.astro.map;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hedenus.astro.Constellation;
import de.hedenus.astro.common.FileCache;
import de.hedenus.astro.common.Log;

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

		this.starLabels = new StarLabels(settings);
	}

	public MapGeneration draw()
	{
		drawRaster();
		drawBoundaries();

		drawConstellationLines();
		drawStars();
		drawLabels();
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

		int x2 = settings.size.width / 2;
		g2d.drawLine(x2, 0, x2, settings.size.height);
		int d = 15;
		for (int i = d; i <= 180; i += d)
		{
			int w2 = i * settings.size.width / 360;
			g2d.drawOval(x2 - w2, 0, 2 * w2, settings.size.height);
		}

		g2d.clip(new Ellipse2D.Double(0, 0, settings.size.width, settings.size.height));
		for (int i = 0; i < 180; i += d)
		{
			int y = i * settings.size.height / 180;
			g2d.drawLine(0, y, settings.size.width, y);
		}

		return this;
	}

	public MapGeneration drawBoundaries()
	{
		g2d.setColor(settings.boundariesColor);
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

	public MapGeneration drawConstellationLines()
	{
		g2d.setColor(settings.constellationLineColor);
		g2d.setStroke(new BasicStroke(settings.boundariesLineWidth));

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
		g2d.setFont(new Font(settings.starFontName, Font.PLAIN, settings.starFontSize));
		FontMetrics fontMetrics = g2d.getFontMetrics();

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
					System.out.println(bayerDesignation + " " + entry.constellation() + " = " + properName);
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

		return this;
	}

	public MapGeneration drawLabels()
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
