package de.hedenus.astro.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hedenus.astro.geom.Declination;
import de.hedenus.astro.geom.EquatorialCoordinates;
import de.hedenus.astro.geom.RightAscension;

public class OtherObjects implements Serializable
{
	private static final long serialVersionUID = -3675469447001498392L;

	public static final class Entry implements Serializable
	{
		private static final long serialVersionUID = -6311182215078690238L;
		private final String name;
		private final EquatorialCoordinates coordinates;

		public Entry(final String name, final EquatorialCoordinates coordinates)
		{

			this.name = name;
			this.coordinates = coordinates;
		}

		public String name()
		{
			return name;
		}

		public EquatorialCoordinates coordinates()
		{
			return coordinates;
		}
	}

	private final List<OtherObjects.Entry> objects = new ArrayList<>();

	public OtherObjects()
	{
		this.objects.add(new Entry("M 31", new EquatorialCoordinates( //
				RightAscension.fromString("00 42 44.3"), Declination.fromString("41 16 09"))));

		this.objects.add(new Entry("M 33", new EquatorialCoordinates( //
				RightAscension.fromString("01 33 50.9"), Declination.fromString("30 39 37"))));

		this.objects.add(new Entry("M 42", new EquatorialCoordinates( //
				RightAscension.fromString("05 35 16.5"), Declination.fromString("-05 23 23"))));

		this.objects.add(new Entry("M 45", new EquatorialCoordinates( //
				RightAscension.fromString("03 47 24"), Declination.fromString("24 07 00"))));

		this.objects.add(new Entry("\u03c9 Cen", new EquatorialCoordinates( //
				RightAscension.fromString("13 26 45.9"), Declination.fromString("-47 28 37"))));

		this.objects.add(new Entry("47 Tuc", new EquatorialCoordinates( //
				RightAscension.fromString("00 24 05.67"), Declination.fromString("-72 04 52.6"))));

		this.objects.add(new Entry("Nubecula Major", new EquatorialCoordinates( //
				RightAscension.fromString("05 23 34.5"), Declination.fromString("-69 45 22"))));

		this.objects.add(new Entry("Nubecula Minor", new EquatorialCoordinates( //
				RightAscension.fromString("00 52 44.8"), Declination.fromString("-72 49 43"))));

		this.objects.add(new Entry("Sgr A*", new EquatorialCoordinates( //
				RightAscension.fromString("17 45 40"), Declination.fromString("-29 00 28.2"))));

	}

	public List<Entry> objects()
	{
		return objects;
	}
}
