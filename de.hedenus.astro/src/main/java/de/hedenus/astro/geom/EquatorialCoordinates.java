package de.hedenus.astro.geom;

public class EquatorialCoordinates extends CelestialCoordinates
{
	private static final long serialVersionUID = 7735494041627406527L;

	public static EquatorialCoordinates fromRectangularCoordinates(final double[] v)
	{
		double ra = 0.0;
		double dec = (Math.PI / 2.0);
		if (v[2] < 0.0)
		{
			dec = -dec;
		}
		if (v[1] != 0.0 || v[0] != 0.0)
		{
			ra = Math.atan2(v[1], v[0]);
			dec = Math.atan(v[2] / Math.sqrt(v[0] * v[0] + v[1] * v[1]));
		}

		return new EquatorialCoordinates(RightAscension.fromDecimalDegree(Math.toDegrees(ra)),
				Declination.fromDecimalDegree(Math.toDegrees(dec)));
	}

	public EquatorialCoordinates(final RightAscension ra, final Declination dec)
	{
		super(ra, dec);
	}

	public RightAscension ra()
	{
		return (RightAscension) x;
	}

	public Declination dec()
	{
		return (Declination) y;
	}

	@Override
	public String toString()
	{
		return "RA " + ra() + " DEC " + dec();
	}

	public double[] toRectangularCoordinates()
	{

		//         north
		//         pole
		//           Z
		//           |
		//           |   o
		//           |  /|
		//           | / |
		//           |/d)|___________Y
		//           /\  |  /
		//          /a \ | /
		//         /____\|/
		//        /
		//       X
		//     vernal
		//    equinox

		double x = dec().cos() * ra().cos();
		double y = dec().cos() * ra().sin();
		double z = dec().sin();

		return new double[]
		{ x, y, z };
	}
}