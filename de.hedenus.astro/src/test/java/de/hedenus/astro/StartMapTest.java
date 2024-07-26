package de.hedenus.astro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import de.hedenus.astro.map.Label;
import de.hedenus.astro.map.MapProjection;
import de.hedenus.astro.map.Settings;
import de.hedenus.astro.map.StarCatalogue;

public class StartMapTest
{
	@Test
	public void test4()
	{
		System.out.println(new StarCatalogue().stars(Settings.defaultSettings(0, false).starMaxMagnitude).count());
	}

	@Test
	public void test5()
	{
		MapProjection mapProjection = new MapProjection(new Dimension(2000, 1000));

		System.out.println(mapProjection.inverseMollweide(500, 500));
		System.out.println(mapProjection.inverseMollweide(750, 500));
		System.out.println(mapProjection.inverseMollweide(1000, 500));
		System.out.println(mapProjection.inverseMollweide(1500, 500));
		System.out.println(mapProjection.inverseMollweide(1750, 500));

		System.out.println(mapProjection.inverseMollweide(1000, 250));
		System.out.println(mapProjection.inverseMollweide(1000, 750));
	}

	@Test
	public void test6() throws Exception
	{
		int w = 500;
		int h = 500;
		int r = 20;
		String s = "\u03b3³";

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();

		Font font = new Font(Font.SERIF, Font.PLAIN, 20);
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, w, h);
		g2d.setColor(Color.red);
		g2d.fillOval((w / 2) - r, (h / 2) - r, 2 * r, 2 * r);
		g2d.setFont(font);

		FontRenderContext frc = g2d.getFontRenderContext();
		int hgap = new TextLayout("|", font, frc).getBounds().getBounds().width;

		TextLayout textLayout = new TextLayout(s, font, frc);

		Rectangle bounds = textLayout.getBounds().getBounds();

		for (int i = 0; i < 8; i++)
		{
			new Label(s, new Point(w / 2, h / 2), r, bounds, font, hgap).position(i).draw(g2d);
		}

		ImageIO.write(bi, "PNG", new File("target/labels.png"));
	}
}
