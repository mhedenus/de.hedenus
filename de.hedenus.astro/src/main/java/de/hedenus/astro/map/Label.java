package de.hedenus.astro.map;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

public class Label implements Serializable
{
	private static final long serialVersionUID = 2869703097276044457L;

	private final Settings settings;
	private final Point p;
	private final int r;
	private final String text;
	private final Rectangle bounds;
	private final Font font;
	private int position = 0;
	private final Rectangle layout = new Rectangle();

	public Label(final Settings settings, final String text, final Point p, final int r, final Rectangle bounds,
			final Font font)
	{
		this.settings = settings;
		this.text = text;
		this.p = p;
		this.r = r;
		this.bounds = bounds;
		this.font = font;
		doLayout();
	}

	public String text()
	{
		return text;
	}

	public void flip()
	{
		position = ((position + 1) % 4);
		doLayout();
	}

	public Rectangle bounds()
	{
		return bounds;
	}

	private void doLayout()
	{
		layout.width = bounds.width;
		layout.height = bounds.height;

		switch (position)
		{
			case 0 ->
			{
				//left
				layout.x = p.x - r - layout.width - settings.starLabelGap;
				layout.y = p.y - (bounds.height / 2);
			}
			case 1 ->
			{
				//right
				layout.x = p.x + r + settings.starLabelGap;
				layout.y = p.y - (bounds.height / 2);
			}
			case 2 ->
			{
				//top
				layout.x = p.x - (bounds.width / 2);
				layout.y = p.y - r - bounds.height;
			}
			case 3 ->
			{
				//bottom
				layout.x = p.x - (bounds.width / 2);
				layout.y = p.y + r;
			}
			default ->
			{
				throw new IllegalStateException();
			}
		}
	}

	public Rectangle layout()
	{
		return layout;
	}

	public void draw(final Graphics2D g2d)
	{
		//g2d.drawRect(layout.x, layout.y, layout.width, layout.height);
		g2d.setFont(font);
		g2d.drawString(text, layout.x, layout.y - bounds.y);
	}

}
