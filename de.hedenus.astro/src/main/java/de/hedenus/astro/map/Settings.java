package de.hedenus.astro.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public final class Settings
{
	public final int dim;
	public Dimension size;
	public int margin;
	public float starMaxMagnitude;
	public float starScale;
	public float rasterLineWidth;
	public float constellationLineWidth;
	public Color constellationLineColor;
	public float boundariesLineWidth;
	public Color frameColor;
	public Color backgroundColor;
	public Color rasterColor;
	public Color boundariesColor;
	public Color starColor;
	public String starFontName;
	public Color starLabelColor;
	public int starFontSize;
	public int starLabelGap;
	public int starLabelLayoutPasses = 4;

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
		settings.starFontSize = Math.round(0.0025f * dim);
		settings.starFontName = Font.SERIF;
		settings.starLabelGap = dim / 2000;

		settings.frameColor = Color.darkGray;
		settings.backgroundColor = Color.black;
		settings.rasterColor = Color.darkGray;
		settings.boundariesColor = Color.lightGray;
		settings.constellationLineColor = Color.green;
		settings.starColor = Color.white;
		settings.starLabelColor = Color.red;

		return settings;
	}
}