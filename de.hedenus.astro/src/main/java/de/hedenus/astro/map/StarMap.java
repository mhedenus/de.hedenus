package de.hedenus.astro.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.hedenus.astro.AstroException;
import de.hedenus.astro.common.Log;

public class StarMap
{
	private final Settings settings;
	private final BufferedImage image;
	private final Graphics2D graphics2d;

	public StarMap(final Settings settings)
	{
		this.settings = settings;
		this.image = new BufferedImage(settings.size.width + 2 * settings.margin,
				settings.size.height + 2 * settings.margin, BufferedImage.TYPE_INT_ARGB);
		this.graphics2d = initGraphics2D();
	}

	private Graphics2D initGraphics2D()
	{
		Graphics2D graphics2d = image.createGraphics();
		graphics2d.setColor(settings.frameColor);
		graphics2d.fillRect(0, 0, this.image.getWidth(), this.image.getHeight());
		graphics2d.translate(settings.margin, settings.margin);
		return graphics2d;
	}

	public Graphics2D graphics2d()
	{
		return graphics2d;
	}

	public void save(final File file)
	{
		BufferedImage result = image;

		if (settings.supersampling)
		{
			long t0 = System.currentTimeMillis();

			final int w2 = image.getWidth() / 2;
			final int h2 = image.getHeight() / 2;
			BufferedImage im2 = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_ARGB);
			for (int x = 0; x < w2; x++)
			{
				for (int y = 0; y < h2; y++)
				{
					int x2 = 2 * x;
					int y2 = 2 * y;
					int rgb1 = image.getRGB(x2 + 0, y2 + 0);
					int rgb2 = image.getRGB(x2 + 1, y2 + 0);
					int rgb3 = image.getRGB(x2 + 0, y2 + 1);
					int rgb4 = image.getRGB(x2 + 1, y2 + 1);
					int r = 0xFF0000
							& (((rgb1 & 0xFF0000) + (rgb2 & 0xFF0000) + (rgb3 & 0xFF0000) + (rgb4 & 0xFF0000)) >> 2);
					int g = 0x00FF00
							& (((rgb1 & 0x00FF00) + (rgb2 & 0x00FF00) + (rgb3 & 0x00FF00) + (rgb4 & 0x00FF00)) >> 2);
					int b = 0x0000FF
							& (((rgb1 & 0x0000FF) + (rgb2 & 0x0000FF) + (rgb3 & 0x0000FF) + (rgb4 & 0x0000FF)) >> 2);
					int rgb = 0xFF000000 + r + g + b;
					im2.setRGB(x, y, rgb);
				}
			}
			result = im2;

			long t1 = System.currentTimeMillis();
			Log.info("Supersampling done in " + (t1 - t0) / 1000.0f + "s");
		}

		try
		{
			ImageIO.write(result, "PNG", file);
		}
		catch (IOException ex)
		{
			throw new AstroException(ex);
		}
	}

}
