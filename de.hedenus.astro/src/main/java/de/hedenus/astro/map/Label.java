package de.hedenus.astro.map;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

public class Label implements Serializable
{
	private static final long serialVersionUID = 2869703097276044457L;

	private final Point p;
	private final int r;
	private final String text;
	private final Rectangle bounds;
	private final Font font;
	private final int hgap;
	private int position = 0;
	private final Rectangle layout = new Rectangle();

	public Label(final String text, final Point p, final int r, final Rectangle bounds, final Font font, final int hgap)
	{
		this.text = text;
		this.p = p;
		this.r = r;
		this.bounds = bounds;
		this.font = font;
		this.hgap = hgap;
		doLayout();
	}

	public String text()
	{
		return text;
	}

	public int position()
	{
		return position;
	}

	public Label position(final int p)
	{
		this.position = p;
		doLayout();
		return this;
	}

	public void flip()
	{
		position = ((position + 4) % 8);
		doLayout();
	}

	public void rotate()
	{
		position = ((position + 1) % 8);
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

		final int r2 = Math.round(0.5f * (float) Math.sqrt(2.0) * r);

		int i = position >= 0 ? position : -position;
		switch (i)
		{
			case 0 ->
			{
				//left
				layout.x = p.x - r - layout.width - hgap;
				layout.y = p.y - (bounds.height / 2);
			}
			case 1 ->
			{
				//bottom left
				layout.x = p.x - r2 - layout.width;
				layout.y = p.y + r2;
			}
			case 2 ->
			{
				//bottom
				layout.x = p.x - (bounds.width / 2);
				layout.y = p.y + r;
			}
			case 3 ->
			{
				//bottom right
				layout.x = p.x + r2;
				layout.y = p.y + r2;
			}
			case 4 ->
			{
				//right
				layout.x = p.x + r + hgap;
				layout.y = p.y - (bounds.height / 2);
			}
			case 5 ->
			{
				//top right
				layout.x = p.x + r2;
				layout.y = p.y - r2 - layout.height;
			}
			case 6 ->
			{
				//top
				layout.x = p.x - (bounds.width / 2);
				layout.y = p.y - r - bounds.height;
			}
			case 7 ->
			{
				//top left
				layout.x = p.x - r2 - layout.width;
				layout.y = p.y - r2 - layout.height;
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
		this.draw(g2d, this.layout);
	}

	public void draw(final Graphics2D g2d, final Rectangle layout)
	{
		//g2d.drawRect(layout.x, layout.y, layout.width, layout.height);
		g2d.setFont(font);
		g2d.drawString(text, layout.x, layout.y - bounds.y);
	}

}
