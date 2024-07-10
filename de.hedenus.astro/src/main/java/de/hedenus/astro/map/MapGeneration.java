package de.hedenus.astro.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
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

		new MapGeneration(10000).draw().save();

		Log.info("Done in " + ((System.currentTimeMillis() - t0) / 1000.0f) + "s");
	}

	private final int dim;
	private final Dimension size;
	private final float starScale;
	private final float rasterLineWidth;
	private final float constellationLineWidth;
	private final StarMap map;
	private final Graphics2D g2d;
	private final MapProjection mapProjection;
	private final StarCatalogue starCatalogue;
	private final ConstellationLines constellationLines;

	public MapGeneration(final int dim)
	{
		this.dim = dim;
		this.size = new Dimension(dim, dim / 2);
		this.starScale = dim / 3000.0f;
		this.rasterLineWidth = dim / 5000.0f;
		this.constellationLineWidth = dim / 4000.0f;

		this.map = new StarMap(size, 10);
		this.g2d = map.graphics2d();
		this.mapProjection = new MapProjection(size);

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
		g2d.setColor(Color.white);
		g2d.fill(new Ellipse2D.Double(0, 0, size.width, size.height));
		g2d.setColor(Color.lightGray);
		g2d.setStroke(new BasicStroke(rasterLineWidth));

		int x2 = size.width / 2;
		g2d.drawLine(x2, 0, x2, size.height);
		int d = 15;
		for (int i = d; i <= 180; i += d)
		{
			int w2 = i * size.width / 360;
			g2d.drawOval(x2 - w2, 0, 2 * w2, size.height);
		}

		g2d.clip(new Ellipse2D.Double(0, 0, size.width, size.height));
		for (int i = 0; i < 180; i += d)
		{
			int y = i * size.height / 180;
			g2d.drawLine(0, y, size.width, y);
		}

		return this;
	}

	public MapGeneration drawBoundaries()
	{
		g2d.setColor(Color.darkGray);
		g2d.setStroke(new BasicStroke(rasterLineWidth));

		Set<SphericalLine> linesDuplicateFilter = new HashSet<>();

		List<Constellation> constellationList = List.of(Constellation.values());
		for (Constellation constellation : constellationList)
		{
			ConstellationBoundaries constellationBoundaries = ConstellationBoundaries.boundaries(constellation);

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
		g2d.setStroke(new BasicStroke(constellationLineWidth));

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
		g2d.setColor(Color.black);

		for (StarCatalogue.Entry entry : starCatalogue.entries())
		{
			if (entry.isStar())
			{
				if (entry.apparentMagnitude() < 6.5f)
				{
					Point px = mapProjection.project(entry.coordinates());

					int size = Math.round(starScale * (8.0f - entry.apparentMagnitude()));
					int size2 = size / 2;

					g2d.fillOval(px.x - size2, px.y - size2, size, size);
				}
			}
		}
		return this;
	}

	public MapGeneration save()
	{
		map.save(new File("target", "starMap_" + dim + ".png"));
		return this;
	}
}
