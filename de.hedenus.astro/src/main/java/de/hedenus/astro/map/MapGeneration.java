package de.hedenus.astro.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.util.ArrayList;
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

		new MapGeneration(4000).draw().save();

		Log.info("Done in " + ((System.currentTimeMillis() - t0) / 1000.0f) + "s");
	}

	public static final class Settings
	{
		public final int dim;
		public Dimension size;
		public int margin;
		public float starMaxMagnitude;
		public float starScale;
		public float rasterLineWidth;
		public float constellationLineWidth;
		public float boundariesLineWidth;
		public Color frameColor;
		public Color backgroundColor;
		public Color rasterColor;
		public Color boundariesColor;
		public Color starColor;
		public Color starLabelColor;
		public int starFontSize;
		public Font starFont;

		public Settings(final int dim)
		{
			this.dim = dim;
		}

		public static Settings defaultSettings(final int dim)
		{
			Settings settings = new Settings(dim);

			settings.size = new Dimension(dim, dim / 2);
			settings.margin = settings.dim / 100;
			settings.starMaxMagnitude = 6.5f;
			settings.starScale = dim / 3000.0f;
			settings.rasterLineWidth = dim / 5000.0f;
			settings.constellationLineWidth = dim / 4000.0f;
			settings.boundariesLineWidth = settings.constellationLineWidth;
			settings.backgroundColor = Color.white;
			settings.rasterColor = Color.lightGray;
			settings.boundariesColor = Color.darkGray;
			settings.starColor = Color.black;
			settings.starLabelColor = Color.red;
			settings.starFontSize = Math.round(0.0025f * dim);
			settings.starFont = new Font(Font.SERIF, Font.PLAIN, settings.starFontSize);

			return settings;
		}
	}

	private final Settings settings;
	private final StarMap map;
	private final Graphics2D g2d;
	private final MapProjection mapProjection;
	private final StarCatalogue starCatalogue;
	private final ConstellationLines constellationLines;
	private final List<Label> labels = new ArrayList<>();

	public MapGeneration(final int dim)
	{
		this.settings = Settings.defaultSettings(dim);
		this.map = new StarMap(settings);
		this.g2d = map.graphics2d();
		this.mapProjection = new MapProjection(settings.size);

		this.starCatalogue = new StarCatalogue();
		this.constellationLines = FileCache.instance().get("constellationLines",
				() -> new ConstellationLines().compute(starCatalogue));
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

		List<Constellation> constellationList = List.of(Constellation.values()); //
		for (Constellation constellation : constellationList)
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
		return this;
	}

	public MapGeneration drawConstellationLines()
	{
		g2d.setColor(Color.blue);
		g2d.setStroke(new BasicStroke(settings.boundariesLineWidth));

		for (Constellation constellation : Constellation.values())
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
		return this;
	}

	public MapGeneration drawStars()
	{
		int[] starCount = new int[1];
		g2d.setColor(settings.starColor);
		g2d.setFont(settings.starFont);
		FontMetrics fontMetrics = g2d.getFontMetrics();

		starCatalogue.stars(settings.starMaxMagnitude).forEach(entry -> {
			Point px = mapProjection.project(entry.coordinates());

			int w = Math.round(settings.starScale * (8.0f - entry.apparentMagnitude()));
			int r = w / 2;

			String name = entry.bayerDesignation();
			if (name != null)
			{
				Rectangle rect = new Rectangle(px.x + r, px.y, fontMetrics.stringWidth(name), fontMetrics.getHeight());
				Label label = new Label(name, rect);
				labels.add(label);
			}

			g2d.fillOval(px.x - r, px.y - r, w, w);
			starCount[0]++;
		});

		Log.info("Stars: " + starCount[0]);
		return this;
	}

	public MapGeneration drawLabels()
	{
		g2d.setColor(settings.starLabelColor);

		for (Label label : labels)
		{
			int x = label.rectangle().x;
			int y = label.rectangle().y;
			g2d.drawString(label.toString(), x, y);
		}

		return this;
	}

	public MapGeneration save()
	{
		map.save(new File("target", "starMap_" + settings.dim + ".png"));
		return this;
	}
}
