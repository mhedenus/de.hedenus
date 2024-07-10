package de.hedenus.astro.geom;

import java.io.Serializable;

public class Location implements Serializable
{
	private static final long serialVersionUID = -2555394301144749963L;

	private String name;
	private final GeoCoordinates coordinates;

	public Location(final String name, final GeoCoordinates coordinates)
	{
		this.name = name;
		this.coordinates = coordinates;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public GeoCoordinates getCoordinates()
	{
		return coordinates;
	}

	@Override
	public String toString()
	{
		return name + " (" + coordinates + ")";
	}
}
