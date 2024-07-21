package de.hedenus.astro.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.hedenus.astro.AstroException;

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
		try
		{
			ImageIO.write(image, "PNG", file);
		}
		catch (IOException ex)
		{
			throw new AstroException(ex);
		}
	}
}
