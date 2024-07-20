package de.hedenus.astro.map;

import java.awt.Rectangle;
import java.io.Serializable;

public class Label implements Serializable
{
	private static final long serialVersionUID = 2869703097276044457L;

	private final String text;
	private final Rectangle rectangle;

	public Label(final String text, final Rectangle rectangle)
	{
		this.text = text;
		this.rectangle = rectangle;
	}

	@Override
	public String toString()
	{
		return text;
	}

	public Rectangle rectangle()
	{
		return rectangle;
	}

}
