package de.hedenus.astro.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public final class Settings
{
	public final int dim;
	public boolean supersampling = true;
	public Dimension size;
	public int margin;
	public float starMaxMagnitude;
	public float starScale;

	public Color backgroundColor;
	public Color frameColor;

	public Color rasterColor;
	public float rasterLineWidth;
	public String rasterLabelFontName;
	public int rasterLabelFontSize;
	public int rasterLabelGap;

	public Color eclipticColor;
	public float eclipticLineWidth;

	public Color constellationLineColor;
	public float constellationLineWidth;
	public String constellationLabelFontName;
	public Color constellationLabelColor;
	public int constellationLabelFontSize;

	public Color constellationBoundariesColor;
	public float constellationBoundariesLineWidth;

	public Color starColor;
	public String starLabelFontName;
	public Color starLabelColor;
	public int starLabelFontSize;
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
		settings.rasterLabelFontName = Font.SERIF;
		settings.rasterLabelFontSize = Math.round(0.0035f * dim);
		settings.rasterLabelGap = dim / 2000;
		settings.constellationLineWidth = dim / 4000.0f;
		settings.constellationBoundariesLineWidth = settings.constellationLineWidth;
		settings.eclipticLineWidth = dim / 3500.0f;

		settings.constellationLabelFontName = Font.SANS_SERIF;
		settings.constellationLabelFontSize = Math.round(0.01f * dim);

		settings.starLabelFontName = Font.SERIF;
		settings.starLabelFontSize = Math.round(0.0025f * dim);
		settings.starLabelGap = dim / 2000;

		settings.frameColor = Color.darkGray;
		settings.backgroundColor = Color.black;
		settings.rasterColor = Color.darkGray;
		settings.eclipticColor = Color.yellow;
		settings.constellationBoundariesColor = Color.lightGray;
		settings.constellationLabelColor = new Color(192, 192, 192, 128);
		settings.constellationLineColor = Color.green;
		settings.starColor = Color.white;
		settings.starLabelColor = Color.red;

		return settings;
	}
}