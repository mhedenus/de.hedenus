package de.hedenus.astro.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;

public final class Settings
{
	public final int dim;
	public final boolean supersampling;
	public Dimension size;
	public int margin;

	public File milkyWay;

	public float starMaxMagnitude;
	public float starScale;

	public Color backgroundColor;
	public Color frameColor;
	public Color chromaKey = new Color(0, 255, 0);

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
	public int starLabelLayoutPasses = 6;

	public float otherObjectsLineWidth;
	public String otherObjectsLabelFontName;

	public Settings(final int dim, final boolean supersampling)
	{
		this.dim = dim;
		this.supersampling = supersampling;
	}

	public static Settings defaultSettings(final int dim, final boolean supersampling)
	{
		Settings settings = new Settings(dim, supersampling);

		settings.milkyWay = new File(
				".cache/1567215018748-ESA_Gaia_DR2_AllSky_Brightness_Colour_Cartesian_2000x1000.png");
		settings.milkyWay = new File(
				".cache/1567215018536-ESA_Gaia_DR2_AllSky_Brightness_Colour_Cartesian_4000x2000.png");
		settings.milkyWay = new File(".cache/milkyway_2020_4k_print.jpg");
		settings.milkyWay = new File(".cache/gaia_20k.jpg"); //
		settings.milkyWay = new File(".cache/Gaia_36k.bmp");

		settings.size = new Dimension(dim, dim / 2);
		settings.margin = Math.round(0.0075f * settings.dim);
		settings.starMaxMagnitude = 6.5f;
		settings.starScale = Math.round(0.00035f * dim);
		settings.rasterLineWidth = Math.round(0.0002f * dim);
		settings.rasterLabelFontName = Font.SERIF;
		settings.rasterLabelFontSize = Math.round(0.0035f * dim);
		settings.rasterLabelGap = Math.round(0.0005f * dim);
		settings.constellationLineWidth = Math.round(0.0002f * dim);
		settings.constellationBoundariesLineWidth = settings.constellationLineWidth;
		settings.eclipticLineWidth = Math.round(0.00025f * dim);

		settings.constellationLabelFontName = Font.SANS_SERIF;
		settings.constellationLabelFontSize = Math.round(0.01f * dim);

		settings.starLabelFontName = Font.SERIF;
		settings.starLabelFontSize = Math.round(0.0035f * dim);
		settings.starLabelGap = Math.round(0.0005f * dim);
		settings.otherObjectsLabelFontName = Font.SANS_SERIF;
		settings.otherObjectsLineWidth = Math.round(0.0002f * dim);

		settings.frameColor = Color.darkGray;
		settings.backgroundColor = Color.black;
		settings.rasterColor = new Color(92, 92, 92);
		settings.eclipticColor = Color.yellow.darker();
		settings.constellationBoundariesColor = Color.lightGray;
		settings.constellationLabelColor = new Color(192, 192, 192, 128);
		settings.constellationLineColor = Color.green;
		settings.starColor = Color.white;
		settings.starLabelColor = Color.red;

		return settings;
	}

	public static Settings lightSettings(final int dim, final boolean supersampling)
	{
		Settings settings = defaultSettings(dim, supersampling);

		settings.frameColor = Color.darkGray;
		settings.backgroundColor = Color.white;
		settings.rasterColor = Color.lightGray;
		settings.eclipticColor = Color.orange;
		settings.constellationBoundariesColor = Color.darkGray;
		settings.constellationLabelColor = new Color(192, 192, 192, 128);
		settings.constellationLineColor = Color.green;
		settings.starColor = Color.black;
		settings.starLabelColor = Color.red;

		return settings;
	}

}