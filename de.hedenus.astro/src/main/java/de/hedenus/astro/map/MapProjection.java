package de.hedenus.astro.map;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hedenus.astro.geom.Angle;
import de.hedenus.astro.geom.DecimalDegree;
import de.hedenus.astro.geom.SphericalCoordinates;

public class MapProjection
{
	private final double w;
	private final double h;
	private double alpha = 0.0;

	public MapProjection(final Dimension size)
	{
		this.w = size.width - 1;
		this.h = size.height - 1;
	}

	public Point project(final SphericalCoordinates x)
	{
		Angle ra = rotate(x.x());
		Angle dec = x.y();

		return mollweide(ra, dec);
	}

	public Point mollweide(final Angle ra, final Angle dec)
	{
		double lam = ra.rad();
		double phi = dec.rad();
		double theta = theta(phi);

		double lam0 = Math.PI; // -pi <= phi < pi
		double sx = (lam - lam0) * Math.cos(theta) / Math.PI;
		double sy = Math.sin(theta);

		int px = (int) Math.round((1.0 * 0.5 * w * sx) + (0.5 * w));
		int py = (int) Math.round((0.5 * h) - (1.0 * 0.5 * h * sy));

		return new Point(px, py);
	}

	public void rotation(final double alpha)
	{
		this.alpha = alpha;
	}

	private Angle rotate(final Angle ra)
	{
		Angle ra2 = new DecimalDegree(Angle.fold360(360.0 - (ra.deg() + 180.0 + alpha))); // Frühlingspunkt in die Mitte, RA von Ost nach Wast
		return ra2;
	}

	private double theta(final double phi)
	{
		double eps = 2 * Math.PI / w / 10.0;

		double t = phi;
		double dt;
		do
		{
			dt = -((2.0 * t + Math.sin(2.0 * t) - Math.PI * Math.sin(phi)) / (4 * Math.cos(t) * Math.cos(t))); // (2.0 + 2.0 * Math.cos(2.0 * t)
			t = t + dt;
		}
		while (Math.abs(dt) > eps);
		return t;
	}

	public List<Line2D> project(final SphericalLine line)
	{
		List<Line2D> result = new ArrayList<>();

		for (SphericalLine splitLine : rotateAndSplit(line))
		{
			interpolate(splitLine, result);
		}

		return result;
	}

	private void interpolate(final SphericalLine line, final List<Line2D> result)
	{
		Point p0 = mollweide(line.x0().x(), line.x0().y());
		Point p1 = mollweide(line.x1().x(), line.x1().y());

		int dy = Math.abs(p1.y - p0.y);
		int intpolStep = 4;
		if (dy > intpolStep)
		{
			double dra = line.x1().x().deg() - line.x0().x().deg();
			double ddec = line.x1().y().deg() - line.x0().y().deg();

			Point i0 = p0;
			for (int y = intpolStep; y <= (dy + intpolStep); y += intpolStep)
			{
				double frac = Math.min(1.0, (double) y / (double) dy);

				double ira1 = line.x0().x().deg() + frac * dra;
				double idec1 = line.x0().y().deg() + frac * ddec;
				Point i1 = mollweide(new DecimalDegree(ira1), new DecimalDegree(idec1));
				result.add(new Line2D(i0, i1));
				i0 = i1;
			}
		}
		else
		{
			result.add(new Line2D(p0, p1));
		}
	}

	private List<SphericalLine> rotateAndSplit(final SphericalLine line)
	{
		List<SphericalLine> result = Collections.emptyList();

		SphericalCoordinates x0 = line.x0();
		SphericalCoordinates x1 = line.x1();

		Angle ra0 = rotate(x0.x());
		Angle ra1 = rotate(x1.x());
		double delta = Math.abs(ra1.deg() - ra0.deg());

		SphericalCoordinates p0 = new SphericalCoordinates(ra0, x0.y());
		SphericalCoordinates p1 = new SphericalCoordinates(ra1, x1.y());

		double eps = 90;

		if (delta > 180.0 && (ra0.deg() < eps) && (ra1.deg() > (360.0 - eps)))
		{
			SphericalCoordinates c1 = new SphericalCoordinates(new DecimalDegree(ra1.deg() - 360.0), x1.y());
			SphericalCoordinates c0 = new SphericalCoordinates(new DecimalDegree(ra0.deg() + 360.0), x0.y());
			result = List.of(new SphericalLine(p0, c1), new SphericalLine(p1, c0));
		}
		else if (delta > 180.0 && (ra1.deg() < eps) && (ra0.deg() > (360.0 - eps)))
		{
			SphericalCoordinates c1 = new SphericalCoordinates(new DecimalDegree(ra1.deg() + 360.0), x1.y());
			SphericalCoordinates c0 = new SphericalCoordinates(new DecimalDegree(ra0.deg() - 360.0), x0.y());
			result = List.of(new SphericalLine(p0, c1), new SphericalLine(p1, c0));
		}
		else
		{

			result = List.of(new SphericalLine(p0, p1));
		}
		return result;
	}

}
