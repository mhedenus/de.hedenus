package de.hedenus.astro.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.hedenus.astro.AstroException;

public class StarMap
{
	private final Dimension size;
	private final int margin;
	private final BufferedImage image;
	private final Graphics2D graphics2d;

	public StarMap(final Dimension size, final int margin)
	{
		this.size = size;
		this.margin = margin;
		this.image = new BufferedImage(size.width + 2 * margin, size.height + 2 * margin, BufferedImage.TYPE_INT_ARGB);
		this.graphics2d = initGraphics2D();
	}

	private Graphics2D initGraphics2D()
	{
		Graphics2D graphics2d = image.createGraphics();
		graphics2d.setColor(Color.lightGray);
		graphics2d.fillRect(0, 0, this.image.getWidth(), this.image.getHeight());
		graphics2d.translate(margin, margin);
		//graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
